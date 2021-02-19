package dev.rosewood.rosecraft.network.packet.server.login

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import java.util.UUID

class PacketLoginOutLoginSuccess(private val uuid: UUID, private val username: String) : ServerPacket(0x02) {

    override fun write(writer: PacketWriter) {
        writer.writeUUID(uuid)
        writer.writeString(username)
    }

}