package dev.rosewood.rosecraft.network.packet.client.handshake

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.player.PlayerConnectionState

class PacketHandshakeInRequest : ClientPacket(0x00) {

    var protocolVersion: Int = -1
        private set
    lateinit var serverAddress: String
        private set
    var serverPort: Int = -1
        private set

    private var nextStateValue = -1
    val nextState: PlayerConnectionState
        get() = when (nextStateValue) {
            1 -> PlayerConnectionState.STATUS
            2 -> PlayerConnectionState.LOGIN
            else -> PlayerConnectionState.UNKNOWN
        }

    override fun read(packetReader: PacketReader) {
        protocolVersion = packetReader.readVarInt()
        serverAddress = packetReader.readString()
        serverPort = packetReader.readUnsignedShort()
        nextStateValue = packetReader.readVarInt()
    }

}