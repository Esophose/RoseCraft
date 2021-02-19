package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketPlayOutPlayerAbilities(
    private val invulnerable: Boolean,
    private val flying: Boolean,
    private val allowFlying: Boolean,
    private val creativeMode: Boolean,
    private val flyingSpeed: Float = 0.05F,
    private val fovModifier: Float = 0.1F
) : ServerPacket(0x30) {

    override fun write(writer: PacketWriter) {
        var flags = 0
        if (invulnerable) flags = flags or 0x1
        if (flying) flags = flags or 0x2
        if (allowFlying) flags = flags or 0x4
        if (creativeMode) flags = flags or 0x8

        writer.writeByte(flags)
        writer.writeFloat(flyingSpeed)
        writer.writeFloat(fovModifier)
    }

}