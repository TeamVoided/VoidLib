package org.teamvoided.voidlib.networking

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

interface S2CVoidPacket<PacketData>: VoidPacket<PacketData> {
    fun handleS2C(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        data: PacketData,
        responseSender: PacketSender
    )

    fun handleS2C(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        handleS2C(client, handler, decode(buf), responseSender)
    }
}

typealias ClientReceiver<PacketData> = S2CVoidPacket<PacketData>