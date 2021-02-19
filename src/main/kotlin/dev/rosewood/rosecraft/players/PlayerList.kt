package dev.rosewood.rosecraft.players

import dev.rosewood.rosecraft.RoseCraft
import dev.rosewood.rosecraft.auth.SessionData
import dev.rosewood.rosecraft.entity.player.Player
import dev.rosewood.rosecraft.network.packet.server.play.*
import dev.rosewood.rosecraft.network.player.PlayerConnection
import dev.rosewood.rosecraft.registry.Identifier
import dev.rosewood.rosecraft.world.Difficulty
import dev.rosewood.rosecraft.world.BlockPosition
import java.util.Collections
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

object PlayerList {

    const val maxPlayers = 10 // TODO: Setting

    val players: MutableSet<Player> = CopyOnWriteArraySet()
    val playersByUUID: MutableMap<UUID, Player> = Collections.synchronizedMap(mutableMapOf())

    val playerCount: Int
        get() = players.size

    fun placeNewPlayer(connection: PlayerConnection) {
        // TODO: Load player data
        val player = Player(connection, connection.sessionData)
        players.add(player)

        playersByUUID[connection.sessionData.id] = player

        //connection.sendPacket(PacketPlayOutJoinGame(connection.playerId))
        connection.sendPacket(PacketPlayOutJoinGame(connection.player.entityId))
        connection.sendPacket(PacketPlayOutPluginMessage(Identifier.of("minecraft:brand"), RoseCraft.serverName.toByteArray()))
        connection.sendPacket(PacketPlayOutServerDifficulty(Difficulty.NORMAL, true)) // TODO
        connection.sendPacket(PacketPlayOutPlayerAbilities(false, false, false, false)) // TODO
        connection.sendPacket(PacketPlayOutSpawnPosition(BlockPosition(0, 0, 0))) // TODO
        connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.PlayerInfoAction.ADD_PLAYER, player)) // TODO: Send to everyone
        connection.sendPacket(PacketPlayOutTimeUpdate(0, 0))

        // TODO: Broadcast player joined
    }

    fun remove(connection: PlayerConnection) {
        val player = players.firstOrNull { it.playerConnection == connection }
        player?.let { remove(it) }
    }

    fun remove(player: Player) {
        // TODO: Save player data
        players.remove(player)
        playersByUUID.remove(player.sessionData.id)
        // TODO: Broadcast player left
    }

    fun canPlayerLogin(connection: PlayerConnection, sessionData: SessionData): LoginStatus {
        // TODO: Bans and whitelist

        // Check player limit
        if (playerCount + 1 > maxPlayers)
            return LoginStatus.BLOCK_FULL

        return LoginStatus.ALLOWED
    }

}