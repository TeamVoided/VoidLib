package org.teamvoided.voidlib.networking

interface ChainS2CVoidPacket<PacketData>: S2CVoidPacket<PacketData>, Chain
typealias ChainClientReceiver<PacketData> = ChainS2CVoidPacket<PacketData>