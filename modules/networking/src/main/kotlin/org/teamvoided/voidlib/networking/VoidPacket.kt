package org.teamvoided.voidlib.networking

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import net.minecraft.network.PacketByteBuf

interface VoidPacket<PacketData> {
    val format: BinaryFormat
    val serializer: KSerializer<PacketData>

    fun encode(buf: PacketByteBuf, data: PacketData) {
        buf.writeByteArray(format.encodeToByteArray(serializer, data))
    }

    fun decode(buf: PacketByteBuf): PacketData {
        return format.decodeFromByteArray(serializer, buf.readByteArray())
    }
}

typealias Receiver<PacketData> = VoidPacket<PacketData>