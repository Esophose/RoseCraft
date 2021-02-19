package dev.rosewood.rosecraft.network.packet.client.login

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket

class PacketLoginInPluginResponse : ClientPacket(0x02) {

    var messageId = -1
        private set
    var successful = false
        private set
    var data: ByteArray? = null
        private set

    override fun read(packetReader: PacketReader) {
        messageId = packetReader.readVarInt()
        successful = packetReader.readBoolean()
        if (successful)
            data = packetReader.readRemainingBytes()
    }

}