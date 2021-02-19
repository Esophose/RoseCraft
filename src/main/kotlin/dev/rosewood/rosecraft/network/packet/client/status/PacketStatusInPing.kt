package dev.rosewood.rosecraft.network.packet.client.status

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket

class PacketStatusInPing : ClientPacket(0x01) {

    var payload = -1L
        private set

    override fun read(packetReader: PacketReader) {
        payload = packetReader.readLong()
    }

}