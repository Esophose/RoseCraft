package dev.rosewood.rosecraft.network.packet.server

import dev.rosewood.rosecraft.network.packet.Packet
import dev.rosewood.rosecraft.network.packet.PacketWriter

abstract class ServerPacket(id: Int) : Packet(id) {

    abstract fun write(writer: PacketWriter)

}
