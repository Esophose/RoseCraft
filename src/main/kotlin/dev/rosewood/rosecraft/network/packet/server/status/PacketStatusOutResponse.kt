package dev.rosewood.rosecraft.network.packet.server.status

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketStatusOutResponse(private val jsonString: String) : ServerPacket(0x00) {

    override fun write(writer: PacketWriter) {
        writer.writeString(jsonString)
    }

}