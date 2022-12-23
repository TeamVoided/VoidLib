package org.team.voided.voidlib.mload.api.obj

import de.javagl.obj.ObjReader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.SpriteAtlasTexture

import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.team.voided.voidlib.mload.MLoadImpl
import org.team.voided.voidlib.mload.api.obj.model.MLoadUnbakedModel


abstract class ObjLoader {
    companion object {
        @JvmStatic val DEFAULT_SPRITE = SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, null)
    }

    protected fun loadObj(manager: ResourceManager, identifier: Identifier, transformation: ModelTransformation, isSideLit: Boolean): UnbakedModel? {
        var id = identifier
        val isBlock = id.path.startsWith("block")

        if (!id.path.endsWith(".obj")) {
            id = Identifier(id.namespace, "${id.path}.obj")
        }

        if (!identifier.path.startsWith("models/")) {
            id = Identifier(identifier.namespace, "models/${identifier.path}")
        }

        val resource = manager.getResource(id)
        if (resource.isPresent) {
            val input = resource.get().inputStream
            val obj = ObjReader.read(input)
            val materials = MLoadImpl.getMaterials(manager, id, obj)

            val textureDependencies = HashSet<SpriteIdentifier>()
            materials.forEach { (_, material) ->
                textureDependencies.add(SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, material.getTexture()))
            }

            val material = materials["sprite"]
            return MLoadUnbakedModel(obj, materials, textureDependencies,
                if (materials.isNotEmpty()) {
                    SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, if (material == null) {
                        materials.values.iterator().next().getTexture()
                    } else material.getTexture())
                } else DEFAULT_SPRITE,
                transformation, isSideLit, isBlock
            )
        }

        return null
    }
}