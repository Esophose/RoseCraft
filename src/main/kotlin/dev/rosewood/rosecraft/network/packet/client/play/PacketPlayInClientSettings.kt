package dev.rosewood.rosecraft.network.packet.client.play

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.player.ChatMode
import dev.rosewood.rosecraft.network.player.DisplayedSkinParts
import dev.rosewood.rosecraft.network.player.HandType

class PacketPlayInClientSettings : ClientPacket(0x05) {

    lateinit var locale: String
        private set
    var viewDistance: Int = -1
        private set
    private var chatModeValue = -1
    val chatMode: ChatMode
        get() = ChatMode.fromId(chatModeValue)
    var chatColors = false
        private set
    private var displayedSkinPartsValue = -1
    val displayedSkinParts: DisplayedSkinParts
        get() = DisplayedSkinParts(displayedSkinPartsValue)
    private var mainHandValue = -1
    val handType: HandType
        get() = HandType.fromId(mainHandValue)

    override fun read(packetReader: PacketReader) {
        locale = packetReader.readString()
        viewDistance = packetReader.readByte().toInt()
        chatModeValue = packetReader.readVarInt()
        chatColors = packetReader.readBoolean()
        displayedSkinPartsValue = packetReader.readUnsignedByte()
        mainHandValue = packetReader.readVarInt()
    }

}
