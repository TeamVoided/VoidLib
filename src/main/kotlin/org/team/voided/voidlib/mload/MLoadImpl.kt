package org.team.voided.voidlib.mload

import de.javagl.obj.*
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.renderer.v1.Renderer
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.Vec3f
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.mload.api.obj.MLoadMaterial
import org.team.voided.voidlib.mload.api.obj.MaterialReader
import org.team.voided.voidlib.mload.impl.ObjLoaderImpl
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Function as JavaFunction


class MLoadImpl: LibModule("MLoad") {
    companion object {
        private val NONE = Vec3f()
        private val BLOCKS = Vec3f(0.5f, 0.5f, 0.5f)

        @JvmStatic val LOGGER: Logger = LoggerFactory.getLogger("MLoad")
        @JvmStatic val MESHES: MutableMap<Identifier, Mesh> = HashMap()

        fun load(
            modelPath: Identifier,
            textureGetter: JavaFunction<SpriteIdentifier, Sprite>,
            bakeSettings: ModelBakeSettings,
            isBlock: Boolean
        ): Mesh? {
            var path = modelPath
            val resourceManager: ResourceManager = MinecraftClient.getInstance().resourceManager
            if (!path.path.endsWith(".obj")) {
                path = Identifier(path.namespace, path.path + ".obj")
            }
            if (!path.path.startsWith("models/")) {
                path = Identifier(path.namespace, "models/" + path.path)
            }
            if (resourceManager.getResource(path).isPresent) {
                try {
                    val inputStream: InputStream = resourceManager.getResource(path).get().inputStream
                    val obj: Obj = ObjReader.read(inputStream)
                    val materials: Map<String, MLoadMaterial> = getMaterials(resourceManager, path, obj)
                    return build(obj, materials, textureGetter, bakeSettings, isBlock)
                } catch (e: IOException) {
                    LOGGER.warn("Failed to load model {}:\n{}", path, e.message)
                }
            }
            return null
        }

        @Throws(IOException::class)
        fun getMaterials(resourceManager: ResourceManager, identifier: Identifier, obj: Obj): Map<String, MLoadMaterial> {
            val materials: MutableMap<String, MLoadMaterial> = LinkedHashMap()
            for (str in obj.mtlFileNames) {
                var path = identifier.path
                path = path.substring(0, path.lastIndexOf('/') + 1) + str
                val resource = Identifier(identifier.namespace, path)
                if (resourceManager.getResource(resource).isPresent) {
                    MaterialReader.read(
                        BufferedReader(
                            InputStreamReader(resourceManager.getResource(resource).get().inputStream)
                        )
                    ).forEach { material -> materials[material.name] = material }
                } else {
                    LOGGER.warn("Texture does not exist: {}", resource)
                }
            }
            return materials
        }

        fun build(
            obj: Obj,
            materials: Map<String, MLoadMaterial>,
            textureGetter: JavaFunction<SpriteIdentifier, Sprite>,
            bakeSettings: ModelBakeSettings,
            isBlock: Boolean
        ): Mesh? {
            val renderer: Renderer = RendererAccess.INSTANCE.renderer ?: return null
            val builder: MeshBuilder = renderer.meshBuilder()
            val emitter: QuadEmitter = builder.emitter
            for (entry in ObjSplitting.splitByMaterialGroups(obj).entries) {
                val group: Obj = entry.value
                var material: MLoadMaterial? = materials[entry.key]
                if (material == null) {
                    material = MLoadMaterial.DEFAULT
                }
                val materialColor: Int = material.getColor()
                val sprite: Sprite =
                    textureGetter.apply(SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, material.getTexture()))
                for (faceIndex in 0 until group.numFaces) {
                    face(
                        renderer,
                        emitter,
                        group,
                        group.getFace(faceIndex),
                        material,
                        materialColor,
                        sprite,
                        bakeSettings,
                        isBlock
                    )
                }
            }
            return builder.build()
        }

        private fun face(
            renderer: Renderer,
            emitter: QuadEmitter,
            group: Obj,
            face: ObjFace,
            material: MLoadMaterial,
            materialColor: Int,
            sprite: Sprite,
            settings: ModelBakeSettings,
            isBlock: Boolean
        ) {
            if (face.numVertices <= 4) {
                for (vertex in 0 until face.numVertices) {
                    vertex(emitter, group, face, vertex, settings, isBlock)
                }
                emit(renderer, emitter, material, materialColor, sprite, settings)
            } else {
                val vertices = face.numVertices
                var textureCoords =
                    if (face.containsTexCoordIndices()) group.getTexCoord(face.getTexCoordIndex(0)) else null

                var pos = of(group.getVertex(face.getVertexIndex(0)))
                pos.add(if (isBlock) BLOCKS else NONE)
                var normal = of(group.getNormal(face.getNormalIndex(0)))
                rotate(settings, pos, normal)
                val start = Vertex(
                    pos,
                    normal,
                    textureCoords?.x ?: 0f,
                    textureCoords?.y ?: 0f
                )
                for (vertex in 1 until vertices - 1) {
                    vertex(emitter, 0, start.pos, start.normal, start.u, start.v)
                    textureCoords =
                        if (face.containsTexCoordIndices()) group.getTexCoord(face.getTexCoordIndex(vertex)) else null
                    pos = of(group.getVertex(face.getVertexIndex(vertex)))
                    pos.add(if (isBlock) BLOCKS else NONE)
                    normal = of(group.getNormal(face.getNormalIndex(vertex)))
                    rotate(settings, pos, normal)
                    vertex(
                        emitter, 1,
                        pos,
                        normal,
                        textureCoords?.x ?: 0f,
                        textureCoords?.y ?: 0f
                    )
                    textureCoords =
                        if (face.containsTexCoordIndices()) group.getTexCoord(face.getTexCoordIndex(vertex + 1)) else null
                    pos = of(group.getVertex(face.getVertexIndex(vertex + 1)))
                    pos.add(if (isBlock) BLOCKS else NONE)
                    normal = of(group.getNormal(face.getNormalIndex(vertex + 1)))
                    rotate(settings, pos, normal)
                    vertex(
                        emitter, 2,
                        pos,
                        normal,
                        textureCoords?.x ?: 0f,
                        textureCoords?.y ?: 0f
                    )
                    vertex(
                        emitter, 3,
                        pos,
                        normal,
                        textureCoords?.x ?: 0f,
                        textureCoords?.y ?: 0f
                    )
                    emit(renderer, emitter, material, materialColor, sprite, settings)
                }
            }
        }

        private fun emit(
            renderer: Renderer,
            emitter: QuadEmitter,
            material: MLoadMaterial,
            materialColor: Int,
            sprite: Sprite,
            settings: ModelBakeSettings
        ) {
            emitter.material(material.getMaterial(renderer))
            emitter.spriteColor(0, materialColor, materialColor, materialColor, materialColor)
            emitter.colorIndex(material.getTintIndex())
            emitter.nominalFace(emitter.lightFace())
            if (material.getCullDirection() != null) {
                emitter.cullFace(material.getCullDirection())
            }
            val bl = settings.isUvLocked || material.isUvLocked()
            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED or if (bl) MutableQuadView.BAKE_LOCK_UV else 0)
            emitter.emit()
        }

        private fun vertex(
            emitter: QuadEmitter,
            group: Obj,
            face: ObjFace,
            vertex: Int,
            settings: ModelBakeSettings,
            isBlock: Boolean
        ) {
            val pos = of(group.getVertex(face.getVertexIndex(vertex)))

            pos.add(if (isBlock) BLOCKS else NONE)
            val normal =
                if (face.containsNormalIndices()) of(group.getNormal(face.getNormalIndex(vertex))) else calculateNormal(
                    group,
                    face
                )
            var u = 0f
            var v = 0f
            if (face.containsTexCoordIndices()) {
                val textureCoords = group.getTexCoord(face.getTexCoordIndex(vertex))
                u = textureCoords.x
                v = 1f - textureCoords.y
                u = if (u > 1 || u < 0) (u % 1f + 1f) % 1f else u
                v = if (v > 1 || v < 0) (v % 1f + 1f) % 1f else v
            }
            rotate(settings, pos, normal)
            vertex(emitter, vertex, pos, normal, u, v)
            if (face.numVertices == 3) {
                vertex(emitter, vertex + 1, pos, normal, u, v)
            }
        }

        private fun calculateNormal(group: Obj, face: ObjFace): Vec3f {
            val p1 = of(group.getVertex(face.getVertexIndex(0)))
            val v1 = of(group.getVertex(face.getVertexIndex(1)))
            val v2 = of(group.getVertex(face.getVertexIndex(2)))
            v1.subtract(p1)
            v2.subtract(p1)
            return Vec3f(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
            )
        }

        private fun rotate(settings: ModelBakeSettings, pos: Vec3f, normal: Vec3f) {
            if (settings.rotation !== AffineTransformation.identity()) {
                pos.add(-0.5f, -0.5f, -0.5f)
                pos.rotate(settings.rotation.rotation2)
                pos.add(0.5f, 0.5f, 0.5f)
                normal.rotate(settings.rotation.rotation2)
            }
        }

        private fun vertex(emitter: QuadEmitter, vertex: Int, pos: Vec3f, normal: Vec3f, u: Float, v: Float) {
            emitter.pos(vertex, pos)
            emitter.normal(vertex, normal)
            emitter.sprite(vertex, 0, u, v)
        }

        private fun of(tuple: FloatTuple): Vec3f {
            return Vec3f(tuple.x, tuple.y, tuple.z)
        }
    }

    override fun commonSetup() { }

    override fun clientSetup() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider { ObjLoaderImpl(it) }
        ModelLoadingRegistry.INSTANCE.registerVariantProvider { ObjLoaderImpl(it) }
        ModelLoadingRegistry.INSTANCE.registerModelProvider { manager, out ->
            val ids: MutableCollection<Identifier> = HashSet()
            val candidates: MutableCollection<Identifier> = ArrayList()

            candidates.addAll(manager.findResources("models/block") { true }.keys)
            candidates.addAll(manager.findResources("models/item") { true }.keys)
            candidates.addAll(manager.findResources("models/misc") { true }.keys)

            for (id in candidates) {
                if (id.path.endsWith(".obj")) {
                    ids.add(id)
                    ids.add(Identifier(id.namespace, id.path.substring(0, id.path.indexOf(".obj"))))
                } else {
                    val test = Identifier(id.namespace, id.path + ".obj")
                    if (manager.getResource(test).isPresent) {
                        ids.add(id)
                    }
                }
            }

            ids.forEach { id ->
                val path: String = id.path
                if (path.startsWith("models/")) {
                    out.accept(Identifier(id.namespace, path.substring("models/".length)))
                }
                out.accept(id)
            }
        }

        LOGGER.info("Loaded VoidLib: MLoad")
    }

    private data class Vertex constructor(val pos: Vec3f, val normal: Vec3f, val u: Float, val v: Float)
}
