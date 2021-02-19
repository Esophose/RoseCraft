package dev.rosewood.rosecraft.network.packet.server.login

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.registry.Identifier

class PacketLoginOutLoginPluginRequest(
    private val messageId: Int,
    private val channel: Identifier,
    private val data: ByteArray
) : ServerPacket(0x04) {

    override fun write(writer: PacketWriter) {
        writer.writeVarInt(messageId)
        writer.writeString(channel.toString())
        writer.writeBytes(data)
    }

}