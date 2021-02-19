package dev.rosewood.rosecraft.network.packet.client.handler

import dev.rosewood.rosecraft.RoseCraft
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.packet.client.play.PacketPlayInClientSettings
import dev.rosewood.rosecraft.network.packet.client.play.PacketPlayInKeepAlive
import dev.rosewood.rosecraft.network.packet.client.play.PacketPlayInPluginMessage
import dev.rosewood.rosecraft.network.player.ClientSettings
import dev.rosewood.rosecraft.network.player.PlayerConnection
import dev.rosewood.rosecraft.registry.Identifier

object ClientPlayPacketHandler : ClientPacketHandler() {

    init {
        register(0x05, PacketPlayInClientSettings::class, ::clientSettingsListener)
        register(0x0B, PacketPlayInPluginMessage::class, ::pluginMessageListener)
        register(0x10, PacketPlayInKeepAlive::class, ::keepAliveListener)
    }

    private fun clientSettingsListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketPlayInClientSettings

        val clientSettings = ClientSettings(
            packet.locale,
            packet.viewDistance,
            packet.chatMode,
            packet.chatColors,
            packet.displayedSkinParts,
            packet.handType
        )
        playerConnection.player.clientSettings = clientSettings
    }

    private fun pluginMessageListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketPlayInPluginMessage

        // By default, the server only handles the minecraft:brand channel, all others are ignored
        if (packet.channel == Identifier.of("minecraft:brand"))
            playerConnection.player.clientBrand = String(packet.data)
    }

    private fun keepAliveListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketPlayInKeepAlive

        if (RoseCraft.lastKeepAliveId == packet.keepAliveId)
            playerConnection.player.keepAliveFailures = 0
    }

}
