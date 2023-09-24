package org.teamvoided.voidlib.networking

interface ChainC2SVoidPacket<PacketData>: C2SVoidPacket<PacketData>, Chain
typealias ChainServerReciever<PacketData> = ChainC2SVoidPacket<PacketData>