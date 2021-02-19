package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketPlayOutTimeUpdate(private val worldAge: Long, private val timeOfDay: Long) : ServerPacket(0x4E) {

    override fun write(writer: PacketWriter) {
        writer.writeLong(worldAge)
        writer.writeLong(timeOfDay)
    }

}
