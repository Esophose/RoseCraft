package dev.rosewood.rosecraft.`network-old`

import java.net.ServerSocket

class PlayerConnectionHandler : Thread("PlayerConnectionHandler") {

    private val port = 25565

    override fun run() {
        val server = ServerSocket(port)
        var playerId = 0
        while (true) {
            val serverClient = server.accept()
            val playerConnection = PlayerConnection(serverClient, playerId)
            playerConnection.start()
            playerId++
        }
    }

}
