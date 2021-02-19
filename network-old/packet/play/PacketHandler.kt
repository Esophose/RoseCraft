package dev.rosewood.rosecraft.`network-old`.packet.play

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket

abstract class PacketHandler<T : InboundPacket> {

    abstract fun handle(packet: T)

}
