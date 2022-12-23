package org.team.voided.voidlib.mload.api

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import org.team.voided.voidlib.mload.MLoadImpl
import org.team.voided.voidlib.mload.impl.mixin.BakedModelManagerAccessor


class MLoad {
    companion object {
        fun getModel(id: Identifier?): BakedModel? {
            return (MinecraftClient.getInstance().bakedModelManager as BakedModelManagerAccessor).models[id]
        }

        fun load(
            modelPath: Identifier,
            textureGetter: (SpriteIdentifier) -> Sprite,
            bakeSettings: ModelBakeSettings,
            isBlock: Boolean
        ): Mesh? {
            return MLoadImpl.load(modelPath, textureGetter, bakeSettings, isBlock)
        }
    }
}