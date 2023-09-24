package org.teamvoided.voidlib.networking

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

interface C2SVoidPacket<PacketData>: VoidPacket<PacketData> {
    fun handleC2S(
        server: MinecraftServer,
        player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler,
        data: PacketData,
        responseSender: PacketSender
    )

    fun handleC2S(
        server: MinecraftServer,
        player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        handleC2S(server, player, handler, decode(buf), responseSender)
    }
}