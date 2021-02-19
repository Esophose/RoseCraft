package dev.rosewood.rosecraft.`network-old`.packet

import dev.rosewood.rosecraft.world.BlockPosition
import java.io.DataInputStream
import java.io.FilterInputStream
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.CipherInputStream

class InboundPacketData(inputStream: DataInputStream, cipher: Cipher? = null) : AutoCloseable {

    private val packetDataStream = if (cipher != null) {
        DataInputStream(CipherInputStream(inputStream, cipher))
    } else {
        inputStream
    }

    val packetLength = readVarInt(inputStream)
    val packetId = readVarInt()

    fun readBoolean(): Boolean {
        return packetDataStream.readBoolean()
    }

    fun readByte(): Byte {
        return packetDataStream.readByte()
    }

    fun readUnsignedByte(): Int {
        return packetDataStream.readUnsignedByte()
    }

    fun readBytes(amount: Int): ByteArray {
        val bytes = ByteArray(amount)
        for (i in 0 until amount)
            bytes[i] = packetDataStream.readByte()
        return bytes
    }

    fun readShort(): Short {
        return packetDataStream.readShort()
    }

    fun readUnsignedShort(): Int {
        return packetDataStream.readUnsignedShort()
    }

    fun readChar(): Char {
        return packetDataStream.readChar()
    }

    fun readInt(): Int {
        return packetDataStream.readInt()
    }

    fun readVarInt(): Int {
        return readVarInt(packetDataStream)
    }

    fun readLong(): Long {
        return packetDataStream.readLong()
    }

    fun readVarLong(): Long {
        var numRead = 0
        var result = 0L
        var read: Int
        do {
            read = packetDataStream.readUnsignedByte()
            val value: Int = read and 0b01111111
            result = result or (value shl (7 * numRead)).toLong()
            numRead++
            if (numRead > 10)
                throw RuntimeException("VarLong is too big")
        } while ((read and 0b10000000) != 0)
        return result
    }

    fun readFloat(): Float {
        return packetDataStream.readFloat()
    }

    fun readDouble(): Double {
        return packetDataStream.readDouble()
    }

    fun readString(): String {
        val strLength = readVarInt()
        val strBytes = ByteArray(strLength)
        for (i in 0 until strLength)
            strBytes[i] = packetDataStream.readByte()
        return String(strBytes)
    }

    fun readPosition(): BlockPosition {
        val encoded = packetDataStream.readLong()
        val x = (encoded shr 38).toInt()
        val y = (encoded and 0xFFF).toInt()
        val z = (encoded shl 26 shr 38).toInt()
        return BlockPosition(x, y, z)
    }

    fun readUUID(): UUID {
        val msb = packetDataStream.readLong()
        val lsb = packetDataStream.readLong()
        return UUID(msb, lsb)
    }

    fun available(): Int {
        return packetDataStream.available()
    }

    override fun close() {
        packetDataStream.close()
    }

    internal companion object {
        fun readVarInt(inputStream: FilterInputStream): Int {
            var numRead = 0
            var result = 0
            var read: Int
            do {
                read = inputStream.read()
                val value: Int = read and 0b01111111
                result = result or (value shl (7 * numRead))
                numRead++
                if (numRead > 5)
                    throw RuntimeException("VarInt is too big")
            } while ((read and 0b10000000) != 0)
            return result
        }
    }

}