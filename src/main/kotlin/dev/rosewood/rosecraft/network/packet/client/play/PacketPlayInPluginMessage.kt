package dev.rosewood.rosecraft.network.packet.client.play

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.registry.Identifier

class PacketPlayInPluginMessage : ClientPacket(0x0B) {

    private lateinit var channelValue: String
    val channel: Identifier
        get() = Identifier.of(channelValue)
    lateinit var data: ByteArray

    override fun read(packetReader: PacketReader) {
        channelValue = packetReader.readString()
        data = packetReader.readRemainingBytes()
    }

}
