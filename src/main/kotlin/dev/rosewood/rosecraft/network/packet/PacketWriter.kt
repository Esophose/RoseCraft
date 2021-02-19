package dev.rosewood.rosecraft.network.packet

import dev.rosewood.rosecraft.world.BlockPosition
import io.netty.buffer.Unpooled
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTWriter
import java.io.OutputStream
import java.util.*


class PacketWriter : OutputStream() {

    private val buffer = Unpooled.buffer()
    private val nbtWriter = NBTWriter(this, false)

    fun writeBoolean(value: Boolean) {
        buffer.writeBoolean(value)
    }

    fun writeByte(value: Int) {
        buffer.writeByte(value)
    }

    fun writeBytes(value: ByteArray) {
        buffer.writeBytes(value)
    }

    fun writeShort(value: Int) {
        buffer.writeShort(value)
    }

    fun writeChar(value: Int) {
        buffer.writeChar(value)
    }

    fun writeInt(value: Int) {
        buffer.writeInt(value)
    }

    fun writeLong(value: Long) {
        buffer.writeLong(value)
    }

    fun writeFloat(value: Float) {
        buffer.writeFloat(value)
    }

    fun writeDouble(value: Double) {
        buffer.writeDouble(value)
    }

    fun writeVarInt(value: Int) = PacketUtils.writeVarInt(buffer, value)

    fun writeVarLong(value: Long) = PacketUtils.writeVarLong(buffer, value)

    fun writeString(value: String) = PacketUtils.writeString(buffer, value)

    fun writePosition(value: BlockPosition) = PacketUtils.writePosition(buffer, value)

    fun writeUUID(value: UUID) = PacketUtils.writeUUID(buffer, value)

    fun writeNBT(name: String, nbt: NBT) = nbtWriter.writeNamed(name, nbt)

    fun toByteArray(): ByteArray {
        val length = buffer.readableBytes()
        val bytes = ByteArray(length)
        buffer.getBytes(0, bytes)
        return bytes
    }

    override fun write(b: Int) = writeByte(b)

}
