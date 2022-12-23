package org.team.voided.voidlib.mload.api.obj.model

import com.mojang.datafixers.util.Pair
import de.javagl.obj.Obj
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import org.team.voided.voidlib.mload.MLoadImpl
import org.team.voided.voidlib.mload.api.obj.MLoadMaterial
import java.util.*
import java.util.function.Function
import java.util.function.Function as JavaFunction


class MLoadUnbakedModel(
    private val obj: Obj?,
    private val materials: Map<String, MLoadMaterial>,
    private val textureDependencies: Collection<SpriteIdentifier>,
    private val sprite: SpriteIdentifier,
    private val transformation: ModelTransformation,
    private val isSideLit: Boolean,
    private val isBlock: Boolean): UnbakedModel {

    override fun getModelDependencies(): Collection<Identifier> {
        return Collections.emptyList()
    }

    override fun getTextureDependencies(
        unbakedModelGetter: Function<Identifier, UnbakedModel>,
        unresolvedTextureReferences: MutableSet<Pair<String, String>>
    ): MutableCollection<SpriteIdentifier> {
        return textureDependencies.toMutableList()
    }

    override fun bake(
        loader: ModelLoader,
        textureGetter: JavaFunction<SpriteIdentifier, Sprite>,
        bakeSettings: ModelBakeSettings,
        modelId: Identifier
    ): BakedModel {
        val mesh: Mesh = if (obj == null)
            MLoadImpl.load(
                modelId,
                textureGetter,
                bakeSettings,
                isBlock
            )!! else
            MLoadImpl.build(obj, materials, textureGetter, bakeSettings, isBlock)!!
        MLoadImpl.MESHES[modelId] = mesh
        return MLoadBakedModel(mesh, transformation, textureGetter.apply(sprite), isSideLit)
    }
}