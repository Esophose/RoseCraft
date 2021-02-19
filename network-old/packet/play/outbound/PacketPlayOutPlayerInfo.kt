package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.players.ServerPlayer
import dev.rosewood.rosecraft.world.Gamemode

class PacketPlayOutPlayerInfo(private val action: PlayerInfoAction, private val players: List<ServerPlayer>) : OutboundPacket(0x34) {

    constructor(action: PlayerInfoAction, player: ServerPlayer) : this(action, listOf(player))

    // TODO: Other modes
    override fun write(packetData: OutboundPacketData) {
        packetData.writeVarInt(action.id)
        packetData.writeVarInt(players.size)

        when (action) {
            PlayerInfoAction.ADD_PLAYER -> {
                for (player in players) {
                    val sessionData = player.sessionData
                    packetData.writeUUID(sessionData.id)
                    packetData.writeString(sessionData.name)
                    packetData.writeVarInt(1) // only the texture property
                    packetData.writeString("textures")
                    packetData.writeString(sessionData.textureValue)
                    packetData.writeBoolean(true) // yes, it's signed, we always use encryption for now
                    packetData.writeString(sessionData.textureSignature)
                    packetData.writeVarInt(Gamemode.SURVIVAL.id) // TODO
                    packetData.writeVarInt(0) // TODO: Ping
                    packetData.writeBoolean(false) // has display name
                    //packetData.writeString("") // display name, not provided since above is false
                }
            }
            else -> {} // TODO
        }
    }

    enum class PlayerInfoAction(val id: Int) {

        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4)

    }

}
