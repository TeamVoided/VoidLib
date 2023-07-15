package org.teamvoided.voidlib.dimutil.network

import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World
import org.teamvoided.voidlib.core.id

class DimSyncPacket {
    val newDims: Set<RegistryKey<World>>
    val removedDims: Set<RegistryKey<World>>

    companion object {
        val id = id("dimsync")
    }

    constructor(newDims: Set<RegistryKey<World>>, removedDims: Set<RegistryKey<World>>) {
        this.newDims = newDims
        this.removedDims = removedDims
    }

    constructor(buf: PacketByteBuf) {
        val newDims = HashSet<RegistryKey<World>>()
        val removedDims = HashSet<RegistryKey<World>>()

        val newDimsSize = buf.readVarInt()
        for (i in 0 until newDimsSize) {
            val worldId = buf.readIdentifier()
            newDims.add(RegistryKey.of(RegistryKeys.WORLD, worldId))
        }

        val removedDimsSize = buf.readVarInt()
        for (i in 0 until removedDimsSize) {
            val worldId = buf.readIdentifier()
            removedDims.add(RegistryKey.of(RegistryKeys.WORLD, worldId))
        }

        this.newDims = newDims
        this.removedDims = removedDims
    }

    fun toBytes(buf: PacketByteBuf) {
        buf.writeVarInt(newDims.size)
        newDims.forEach {
            buf.writeIdentifier(it.value)
        }

        buf.writeVarInt(removedDims.size)
        removedDims.forEach {
            buf.writeIdentifier(it.value)
        }
    }

    fun handle(client: MinecraftClient) {
        client.execute {
            val cPlayer = client.player
            if (cPlayer != null) {
                val commandSuggesterLevels = cPlayer.networkHandler.worldKeys
                commandSuggesterLevels.addAll(newDims)
                commandSuggesterLevels.removeAll(removedDims)
            }
        }
    }
}