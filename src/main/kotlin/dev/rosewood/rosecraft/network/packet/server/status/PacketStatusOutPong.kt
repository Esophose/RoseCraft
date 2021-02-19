package dev.rosewood.rosecraft.network.packet.server.status

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketStatusOutPong(private val payload: Long) : ServerPacket(0x01) {

    override fun write(writer: PacketWriter) {
        writer.writeLong(payload)
    }

}
