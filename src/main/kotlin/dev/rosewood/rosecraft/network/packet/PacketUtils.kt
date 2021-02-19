package dev.rosewood.rosecraft.network.packet

import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.world.BlockPosition
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.util.UUID

object PacketUtils {

    //region Writing

    fun writeVarInt(buffer: ByteBuf, value: Int) {
        var writing = value
        do {
            var temp = writing and 0b01111111
            writing = writing ushr 7
            if (writing != 0)
                temp = temp or 0b10000000
            buffer.writeByte(temp)
        } while (writing != 0)
    }

    fun lengthVarInt(value: Int): Int {
        var writing = value
        var length = 0
        do {
            writing = writing ushr 7
            length++
        } while (writing != 0)
        return length
    }

    fun writeVarLong(buffer: ByteBuf, value: Long) {
        var writing = value
        do {
            var temp = writing and 0b01111111
            writing = writing ushr 7
            if (writing != 0L)
                temp = temp or 0b10000000
            buffer.writeByte(temp.toInt())
        } while (writing != 0L)
    }

    fun writeString(buffer: ByteBuf, value: String) {
        val strBytes = value.toByteArray()
        writeVarInt(buffer, strBytes.size)
        buffer.writeBytes(strBytes)
    }

    fun writePosition(buffer: ByteBuf, value: BlockPosition) {
        val encoded: Long = ((value.x and 0x3FFFFFF).toLong() shl 38) or ((value.x and 0x3FFFFFF).toLong() shl 12) or (value.y and 0xFFF).toLong()
        buffer.writeLong(encoded)
    }

    fun writeUUID(buffer: ByteBuf, value: UUID) {
        buffer.writeLong(value.mostSignificantBits)
        buffer.writeLong(value.leastSignificantBits)
    }

    //endregion

    //region Reading

    fun readVarInt(buffer: ByteBuf): Int {
        var numRead = 0
        var result = 0
        var read: Int
        do {
            read = buffer.readByte().toInt()
            val value: Int = read and 0b01111111
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5)
                throw RuntimeException("VarInt is too big")
        } while ((read and 0b10000000) != 0)
        return result
    }

    fun readVarLong(buffer: ByteBuf): Long {
        var numRead = 0
        var result = 0L
        var read: Int
        do {
            read = buffer.readByte().toInt()
            val value: Int = read and 0b01111111
            result = result or (value shl (7 * numRead)).toLong()
            numRead++
            if (numRead > 10)
                throw RuntimeException("VarLong is too big")
        } while ((read and 0b10000000) != 0)
        return result
    }

    fun readString(buffer: ByteBuf): String {
        val strLength = readVarInt(buffer)
        val strBytes = ByteArray(strLength)
        for (i in 0 until strLength)
            strBytes[i] = buffer.readByte()
        return String(strBytes)
    }

    fun readPosition(buffer: ByteBuf): BlockPosition {
        val encoded = buffer.readLong()
        val x = (encoded shr 38).toInt()
        val y = (encoded and 0xFFF).toInt()
        val z = (encoded shl 26 shr 38).toInt()
        return BlockPosition(x, y, z)
    }

    fun readUUID(buffer: ByteBuf): UUID {
        val msb = buffer.readLong()
        val lsb = buffer.readLong()
        return UUID(msb, lsb)
    }

    //endregion

    //region Misc

    fun ensureWritable(buffer: ByteBuf, length: Int): ByteBuf {
        return buffer.ensureWritable(length)
    }

    fun writePacket(packet: ServerPacket): ByteBuf {
        val packetWriter = PacketWriter()
        packetWriter.writeVarInt(packet.id)
        packet.write(packetWriter)

        val bytes = packetWriter.toByteArray()
        val length = bytes.size

        val varIntSize = lengthVarInt(length)

        val buffer = Unpooled.buffer(length + varIntSize)
        writeVarInt(buffer, length)
        buffer.writeBytes(bytes)

        return buffer
    }

    //endregion

}
