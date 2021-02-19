package dev.rosewood.rosecraft.network.packet.server.login

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import java.util.UUID

class PacketLoginOutSetCompression(private val threshold: Int) : ServerPacket(0x03) {

    override fun write(writer: PacketWriter) {
        writer.writeVarInt(threshold)
    }

}