package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketPlayOutKeepAlive(private val keepAliveId: Long) : ServerPacket(0x1F) {

    override fun write(writer: PacketWriter) {
        writer.writeLong(keepAliveId)
    }

}
