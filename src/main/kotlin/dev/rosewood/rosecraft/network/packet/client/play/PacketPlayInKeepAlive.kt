package dev.rosewood.rosecraft.network.packet.client.play

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.registry.Identifier
import org.jglrxavpok.hephaistos.mca.pack

class PacketPlayInKeepAlive : ClientPacket(0x10) {

    var keepAliveId: Long = -1
        private set

    override fun read(packetReader: PacketReader) {
        keepAliveId = packetReader.readLong()
    }

}
