package dev.rosewood.rosecraft.`network-old`.packet.handshake.inbound

import dev.rosewood.rosecraft.`network-old`.PlayerConnectionState
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketHandshakeInRequest(packetData: InboundPacketData) : InboundPacket(0x00, packetData) {

    val protocolVersion = packetData.readVarInt()
    val serverAddress = packetData.readString()
    val serverPort = packetData.readUnsignedShort()
    private val nextStateValue = packetData.readVarInt()

    val nextState: PlayerConnectionState
        get() = when (nextStateValue) {
            1 -> PlayerConnectionState.STATUS
            2 -> PlayerConnectionState.LOGIN
            else -> PlayerConnectionState.CLOSED
        }

}