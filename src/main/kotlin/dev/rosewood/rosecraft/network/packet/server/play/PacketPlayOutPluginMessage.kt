package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.registry.Identifier

class PacketPlayOutPluginMessage(
    private val identifier: Identifier,
    private val data: ByteArray
) : ServerPacket(0x17) {

    override fun write(writer: PacketWriter) {
        writer.writeString(identifier.toString())
        writer.writeBytes(data)
    }

}