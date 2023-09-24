package org.teamvoided.voidlib.networking

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier


@Suppress("unused", "MemberVisibilityCanBePrivate")
object Packets {
    fun <PacketData> sendToClient(
        chainId: Identifier, player: ServerPlayerEntity, packet: S2CVoidPacket<PacketData>, data: PacketData,
    ) {
        val buf = createBuffer()
        packet.encode(buf, data)
        ServerPlayNetworking.send(player, chainId, buf)
    }

    fun <PacketData> sendToClient(
        player: ServerPlayerEntity, packet: ChainS2CVoidPacket<PacketData>, data: PacketData,
    ) {
        val buf = createBuffer()
        packet.encode(buf, data)
        ServerPlayNetworking.send(player, packet.chainId, buf)
    }

    fun <PacketData> sendToServer(chainId: Identifier, packet: C2SVoidPacket<PacketData>, data: PacketData) {
        val buf = PacketByteBuf(Unpooled.buffer())
        packet.encode(buf, data)
        ClientPlayNetworking.send(chainId, buf)
    }

    @JvmName("staticSendToServer")
    fun <PacketData> sendToServer(packet: ChainC2SVoidPacket<PacketData>, data: PacketData) {
        val buf = PacketByteBuf(Unpooled.buffer())
        packet.encode(buf, data)
        ClientPlayNetworking.send(packet.chainId, buf)
    }

    fun <PacketData> S2CVoidPacket<PacketData>.sendToClient(
        chainId: Identifier, player: ServerPlayerEntity, data: PacketData,
    ) = sendToClient(chainId, player, this, data)

    fun <PacketData> ChainS2CVoidPacket<PacketData>.sendToClient(player: ServerPlayerEntity, data: PacketData) =
        sendToClient(player, this, data)

    fun <PacketData> C2SVoidPacket<PacketData>.sendToServer(chainId: Identifier, data: PacketData) =
        sendToServer(chainId, this, data)

    fun <PacketData> ChainC2SVoidPacket<PacketData>.sendToServer(data: PacketData) = sendToServer(this, data)

    fun <PacketData> openChainClient(chain: Chain, packet: S2CVoidPacket<PacketData>) {
        ClientPlayNetworking.registerGlobalReceiver(chain.chainId) { client, handler, buf, responseSender ->
            packet.handleS2C(client, handler, buf, responseSender)
        }
    }

    fun <PacketData> openChainServer(chain: Chain, packet: C2SVoidPacket<PacketData>) {
        ServerPlayNetworking.registerGlobalReceiver(chain.chainId) { server, player, handler, buf, responseSender ->
            packet.handleC2S(server, player, handler, buf, responseSender)
        }
    }

    fun <PacketData> Chain.open(packet: S2CVoidPacket<PacketData>) = openChainClient(this, packet)

    fun <PacketData> Chain.openClient(packet: S2CVoidPacket<PacketData>) = openChainClient(this, packet)

    fun <PacketData> Chain.open(packet: C2SVoidPacket<PacketData>) = openChainServer(this, packet)

    fun <PacketData> Chain.openServer(packet: C2SVoidPacket<PacketData>) = openChainServer(this, packet)

    fun <PacketData> ChainC2SVoidPacket<PacketData>.open() = openChainServer(this, this)

    fun <PacketData> ChainS2CVoidPacket<PacketData>.open() = openChainClient(this, this)

    fun createBuffer() = PacketByteBuf(Unpooled.buffer())
}