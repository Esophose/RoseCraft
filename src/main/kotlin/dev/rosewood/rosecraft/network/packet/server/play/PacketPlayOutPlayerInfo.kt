package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.entity.player.Player
import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.world.Gamemode

class PacketPlayOutPlayerInfo(private val action: PlayerInfoAction, private val players: List<Player>) : ServerPacket(0x32) {

    constructor(action: PlayerInfoAction, player: Player) : this(action, listOf(player))

    // TODO: Other modes
    override fun write(writer: PacketWriter) {
        writer.writeVarInt(action.id)
        writer.writeVarInt(players.size)

        when (action) {
            PlayerInfoAction.ADD_PLAYER -> {
                for (player in players) {
                    val sessionData = player.sessionData
                    writer.writeUUID(sessionData.id)
                    writer.writeString(sessionData.name)
                    writer.writeVarInt(1) // only the texture property
                    writer.writeString("textures")
                    writer.writeString(sessionData.textureValue)
                    writer.writeBoolean(true) // yes, it's signed, we always use encryption for now
                    writer.writeString(sessionData.textureSignature)
                    writer.writeVarInt(Gamemode.SURVIVAL.id) // TODO
                    writer.writeVarInt(0) // TODO: Ping
                    writer.writeBoolean(false) // has display name
                    //writer.writeString("") // display name, not provided since above is false
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
