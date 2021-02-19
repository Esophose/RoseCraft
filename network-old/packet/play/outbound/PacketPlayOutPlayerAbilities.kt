package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketPlayOutPlayerAbilities(
    private val invulnerable: Boolean,
    private val flying: Boolean,
    private val allowFlying: Boolean,
    private val creativeMode: Boolean,
    private val flyingSpeed: Float = 0.05F,
    private val fovModifier: Float = 0.1F
) : OutboundPacket(0x32) {

    override fun write(packetData: OutboundPacketData) {
        var flags = 0
        if (invulnerable) flags = flags or 0x1
        if (flying) flags = flags or 0x2
        if (allowFlying) flags = flags or 0x4
        if (creativeMode) flags = flags or 0x8

        packetData.writeByte(flags)
        packetData.writeFloat(flyingSpeed)
        packetData.writeFloat(fovModifier)
    }

}