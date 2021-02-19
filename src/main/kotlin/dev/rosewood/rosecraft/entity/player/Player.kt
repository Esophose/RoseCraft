package dev.rosewood.rosecraft.entity.player

import dev.rosewood.rosecraft.auth.SessionData
import dev.rosewood.rosecraft.entity.EntityLiving
import dev.rosewood.rosecraft.network.player.ClientSettings
import dev.rosewood.rosecraft.network.player.PlayerConnection

class Player(val playerConnection: PlayerConnection, val sessionData: SessionData) : EntityLiving() {

    var clientSettings: ClientSettings? = null
    var clientBrand: String? = null

    var keepAliveFailures = 0

    fun kick(reason: String? = null) {
        if (reason != null) {
            playerConnection.disconnect(reason)
        } else {
            playerConnection.disconnect()
        }
    }

}
