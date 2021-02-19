package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.world.Difficulty

class PacketPlayOutServerDifficulty(
    private val difficulty: Difficulty,
    private val difficultyLocked: Boolean
) : OutboundPacket(0x0E) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeByte(difficulty.id)
        packetData.writeBoolean(difficultyLocked)
    }

}
