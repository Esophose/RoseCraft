package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketPlayOutDisconnect(private val reason: String) : ServerPacket(0x19) {

    override fun write(writer: PacketWriter) {
        val json = """
            {
                "text": "$reason"
            }
        """.trimIndent()
        writer.writeString(json)
    }

}
