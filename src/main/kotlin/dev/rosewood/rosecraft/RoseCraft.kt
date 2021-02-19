package dev.rosewood.rosecraft

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.rosewood.rosecraft.network.netty.NettyServer
import dev.rosewood.rosecraft.network.packet.server.play.PacketPlayOutKeepAlive
import dev.rosewood.rosecraft.players.PlayerList
import java.util.*

object RoseCraft {

    const val gameVersion = "1.16.4"
    const val protocol = 754
    const val serverName = "RoseCraft"

    private const val ticksPerSecond = 20
    private const val tickLength = 1000000000 / ticksPerSecond

    private var running = true

    var lastTick = 0L
        private set

    val shouldPreventProxy = false // TODO: Setting

    //region Keep Alive

    private const val keepAliveTicks = 60L // every 3 seconds
    private const val maxKeepAliveFailures = ((30 * 20) / keepAliveTicks).toInt() + 1
    private var lastKeepAliveTick = 0L
    var lastKeepAliveId = -1L
        private set

    //endregion

    //region Managers

    val nettyServer = NettyServer()

    //endregion

    internal fun run(args: Array<String>) {
        nettyServer.start("127.0.0.1", 25565)

        heartbeat()
        shutdown()
    }

    private fun heartbeat() {
        while (running) {
            tick()

            val lastLoopTime = System.nanoTime()
            while (System.nanoTime() - lastLoopTime < tickLength)
                Thread.sleep(1)
        }
    }

    private fun tick() {
        lastTick++

        // Client keepalive handling
        if (lastTick - lastKeepAliveTick >= keepAliveTicks) {
            lastKeepAliveTick = lastTick
            lastKeepAliveId = System.currentTimeMillis()

            val keepAlivePacket = PacketPlayOutKeepAlive(lastKeepAliveId)

            for (player in PlayerList.players) {
                if (player.keepAliveFailures >= maxKeepAliveFailures) {
                    player.playerConnection.disconnect("No keep alive received for over 30 seconds")
                    continue
                }

                player.playerConnection.sendPacket(keepAlivePacket)
                player.keepAliveFailures++
            }
        }

        // TODO
    }

    private fun shutdown() {
        nettyServer.stop()
        running = false
        // TODO
    }

    fun getStatus(): String {
        val status = JsonObject()

        val version = JsonObject()
        version.addProperty("name", "$serverName $gameVersion")
        version.addProperty("protocol", protocol)
        status.add("version", version)

        val players = JsonObject()
        players.addProperty("max", PlayerList.maxPlayers)
        players.addProperty("online", PlayerList.playerCount)
        val sample = JsonArray()
        for (player in PlayerList.players) {
            val obj = JsonObject()
            obj.addProperty("name", player.sessionData.name)
            obj.addProperty("id", player.sessionData.id.toString())
            sample.add(obj)
        }

        players.add("sample", sample)
        status.add("players", players)

        val description = JsonArray()
        val line1 = JsonObject()
        line1.addProperty("text", "$serverName - Test Server")
        line1.addProperty("color", "#ff4072")
        val line2 = JsonObject()
        line2.addProperty("text", "\nVersion: $gameVersion | Protocol: $protocol")
        line2.addProperty("color", "#c0ffee")
        description.add(line1)
        description.add(line2)
        status.add("description", description)

        val favicon = getFaviconString()
        if (favicon != null)
            status.addProperty("favicon", favicon)

        return status.toString()
    }

    private fun getFaviconString(): String? {
        // TODO: Generate a base64 screen from icon.png
        return "data:image/png;base64,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"
    }

}
