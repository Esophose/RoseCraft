package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.players.PlayerList

class PacketPlayOutJoinGame(private val entityId: Int) : OutboundPacket(0x26) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeInt(entityId)
        packetData.writeByte(0b000) // survival
        packetData.writeInt(0) // overworld
        packetData.writeLong(0b11111111) // seed
        packetData.writeByte(PlayerList.maxPlayers)
        packetData.writeString("default") // level type
        packetData.writeVarInt(12) // render distance
        packetData.writeBoolean(false) // reduced debug
        packetData.writeBoolean(true) // enable respawn screen
    }

}
