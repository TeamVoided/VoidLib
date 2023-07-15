package org.teamvoided.voidlib.vui.v2.rendering.player

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.client.render.entity.PlayerModelPart
import net.minecraft.client.texture.MissingSprite
import net.minecraft.network.ClientConnection
import net.minecraft.network.NetworkSide
import net.minecraft.util.Identifier
import java.time.Duration

open class RenderablePlayerEntity(val profile: GameProfile): ClientPlayerEntity(
    MinecraftClient.getInstance(), MinecraftClient.getInstance().world, ClientPlayNetworkHandler(
        MinecraftClient.getInstance(),
        null,
        ClientConnection(NetworkSide.CLIENTBOUND),
        null,
        profile,
        MinecraftClient.getInstance().telemetryManager.createWorldSession(false, Duration.ZERO, "vui_session")
    ),
    null, null, false, false
) {
    protected val skinTextureId: Identifier
    @get:JvmName("lModel")
    protected val model: String

    init {
        var temp_skinTextureId = MissingSprite.getMissingSpriteId()
        var temp_model = "default"

        client.skinProvider.loadSkin(
            gameProfile,
            { type: MinecraftProfileTexture.Type, identifier: Identifier, texture: MinecraftProfileTexture ->
                if (type != MinecraftProfileTexture.Type.SKIN) return@loadSkin
                temp_skinTextureId = identifier
                temp_model = texture.getMetadata("model") ?: "default"
            }, true
        )

        skinTextureId = temp_skinTextureId
        model = temp_model
    }

    override fun hasSkinTexture(): Boolean = skinTextureId != MissingSprite.getMissingSpriteId()
    override fun getSkinTexture(): Identifier = skinTextureId
    override fun isPartVisible(modelPart: PlayerModelPart?): Boolean = true
    override fun getModel(): String = model
    override fun getPlayerListEntry(): PlayerListEntry? = null
}