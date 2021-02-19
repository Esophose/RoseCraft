package dev.rosewood.rosecraft.network.packet.server.login

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketLoginOutDisconnect(private val reason: String) : ServerPacket(0x00) {

    override fun write(writer: PacketWriter) {
        writer.writeString("""
            {
                "text":"$reason"
            }
        """.trimIndent())
    }

}