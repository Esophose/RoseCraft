package dev.rosewood.rosecraft.network.packet.client.handler

import com.esotericsoftware.reflectasm.ConstructorAccess
import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.player.PlayerConnection
import kotlin.reflect.KClass

abstract class ClientPacketHandler {

    private val constructorAccesses = mutableMapOf<Int, ConstructorAccess<out ClientPacket>>()
    private val listeners = mutableMapOf<Int, (ClientPacket, PlayerConnection) -> Unit>()

    protected fun register(id: Int, packetClass: KClass<out ClientPacket>, listener: (ClientPacket, PlayerConnection) -> Unit) {
        constructorAccesses[id] = ConstructorAccess.get(packetClass.java)
        listeners[id] = listener
    }

    private fun createPacketInstance(id: Int): ClientPacket? {
        val constructorAccess = constructorAccesses[id]
        return constructorAccess?.newInstance()
    }

    fun handle(id: Int, packetReader: PacketReader, target: PlayerConnection) {
        val packet = createPacketInstance(id)
        if (packet != null) {
            packet.read(packetReader)
            listeners[id]?.invoke(packet, target)
        } else {
            packetReader.readRemainingBytes() // dump data
            var idStr = Integer.toHexString(id).toUpperCase()
            if (idStr.length == 1)
                idStr = "0$idStr"
            println("Ignored unhandled packet from ${target.sessionData.name} with ID 0x$idStr")
        }
    }

}
