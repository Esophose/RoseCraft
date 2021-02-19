package dev.rosewood.rosecraft.network.packet

import io.netty.buffer.ByteBuf

class PacketReader(private val buffer: ByteBuf) {

    fun readBoolean(): Boolean {
        return buffer.readBoolean()
    }

    fun readByte(): Byte {
        return buffer.readByte()
    }

    fun readUnsignedByte(): Int {
        return buffer.readUnsignedByte().toInt()
    }

    fun readBytes(amount: Int): ByteArray {
        val bytes = ByteArray(amount)
        for (i in bytes.indices)
            bytes[i] = buffer.readByte()
        return bytes
    }

    fun readShort(): Short {
        return buffer.readShort()
    }

    fun readUnsignedShort(): Int {
        return buffer.readUnsignedShort()
    }

    fun readChar(): Char {
        return buffer.readChar()
    }

    fun readInt(): Int {
        return buffer.readInt()
    }

    fun readLong(): Long {
        return buffer.readLong()
    }

    fun readFloat(): Float {
        return buffer.readFloat()
    }

    fun readDouble(): Double {
        return buffer.readDouble()
    }

    fun readRemainingBytes(): ByteArray {
        val bytes = ByteArray(buffer.readableBytes())
        for (i in bytes.indices)
            bytes[i] = buffer.readByte()
        return bytes
    }

    fun readVarInt() = PacketUtils.readVarInt(buffer)

    fun readVarLong() = PacketUtils.readVarLong(buffer)

    fun readString() = PacketUtils.readString(buffer)

    fun readPosition() = PacketUtils.readPosition(buffer)

    fun readUUID() = PacketUtils.readUUID(buffer)

}
