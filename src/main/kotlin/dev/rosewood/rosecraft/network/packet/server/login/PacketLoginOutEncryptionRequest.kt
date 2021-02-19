package dev.rosewood.rosecraft.network.packet.server.login

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketLoginOutEncryptionRequest(
    private val serverId: String,
    private val publicKey: ByteArray,
    private val verifyToken: ByteArray
) : ServerPacket(0x01) {

    override fun write(writer: PacketWriter) {
        writer.writeString(serverId)
        writer.writeVarInt(publicKey.size)
        writer.writeBytes(publicKey)
        writer.writeVarInt(verifyToken.size)
        writer.writeBytes(verifyToken)
    }

}