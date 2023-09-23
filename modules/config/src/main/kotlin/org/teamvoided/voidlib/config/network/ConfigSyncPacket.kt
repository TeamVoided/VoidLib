package org.teamvoided.voidlib.config.network

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.config.ConfigManager
import org.teamvoided.voidlib.config.VoidFig
import org.teamvoided.voidlib.config.VoidFigHelpers
import org.teamvoided.voidlib.config.screen.ConfigSyncScreen
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.core.id

class ConfigSyncPacket: ClientPlayNetworking.PlayChannelHandler, (MinecraftServer, ServerPlayerEntity, ServerPlayNetworkHandler, PacketByteBuf, PacketSender) -> Unit {
    companion object {
        val id = id("config_sync_packet")

        fun serverHandshakeInit(buf: PacketByteBuf): PacketByteBuf {
            buf.writeByte(ConfigSyncState.SERVER_HANDSHAKE_INIT.ordinal)
            buf.writeMap(ConfigManager.commonConfigs, { buf2, key ->
                buf2.writeIdentifier(key)
            }) { buf2, value ->
                buf2.writeString(VoidFigHelpers.getConfigData(value))
            }

            return buf
        }

        fun clientHandshakeEnd(buf: PacketByteBuf, configChanged: Boolean): PacketByteBuf {
            buf.writeByte(ConfigSyncState.SERVER_HANDSHAKE_INIT.ordinal)
            buf.writeBoolean(configChanged)

            return buf
        }
    }

    override fun receive(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        val ordinal = buf.readByte().i
        val state = ConfigSyncState.entries[ordinal]

        if (state == ConfigSyncState.SERVER_HANDSHAKE_INIT) {
            var currentId: Identifier = id("null_config")

            val notMatching: MutableMap<Identifier, String> = HashMap()

            val serverConfigs: MutableMap<Identifier, VoidFig> =
                buf.readMap({
                    currentId = it.readIdentifier()
                    currentId
                }) {
                    val voidFig = ConfigManager.commonConfigs[currentId]!!

                    val rawData = it.readString()

                    if (!voidFig.matchesRawData(rawData)) {
                        notMatching[currentId] = rawData
                    }

                    voidFig
                }

            responseSender.sendPacket(id, clientHandshakeEnd(PacketByteBuf(Unpooled.buffer()), notMatching.isNotEmpty()))

            if (notMatching.isNotEmpty()) MinecraftClient.getInstance().setScreen(ConfigSyncScreen(notMatching))
        }
    }

    override fun invoke(
        server: MinecraftServer,
        player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler,
        buf: PacketByteBuf,
        sender: PacketSender
    ) {
        val ordinal = buf.readByte().i
        val state = ConfigSyncState.entries[ordinal]

        if (state == ConfigSyncState.CLIENT_HANDSHAKE_END && buf.readBoolean()) {
            player.networkHandler.disconnect(Text.empty())
        }
    }
}