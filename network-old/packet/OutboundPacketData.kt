package dev.rosewood.rosecraft.`network-old`.packet

import dev.rosewood.rosecraft.world.BlockPosition
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.FilterOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.NullCipher

class OutboundPacketData : AutoCloseable {

    val byteArrayOutputStream = ByteArrayOutputStream()
    private val outputStream = DataOutputStream(byteArrayOutputStream)

    fun apply(outputStream: OutputStream, cipher: Cipher? = null) {
        val cipherOutputStream = CipherOutputStream(outputStream, cipher ?: NullCipher())

        this.outputStream.flush()

        writeVarInt(byteArrayOutputStream.size(), cipherOutputStream)
        byteArrayOutputStream.writeTo(cipherOutputStream)
        cipherOutputStream.flush()
        close()
    }

    fun writeBoolean(value: Boolean) {
        outputStream.writeBoolean(value)
    }

    fun writeByte(value: Int) {
        outputStream.write(value)
    }

    fun writeBytes(value: ByteArray) {
        outputStream.write(value)
    }

    fun writeShort(value: Int) {
        outputStream.writeShort(value)
    }

    fun writeChar(value: Int) {
        outputStream.writeChar(value)
    }

    fun writeInt(value: Int) {
        outputStream.writeInt(value)
    }

    fun writeVarInt(value: Int) {
        writeVarInt(value, outputStream)
    }

    fun writeLong(value: Long) {
        outputStream.writeLong(value)
    }

    fun writeVarLong(value: Long) {
        var writing = value
        do {
            var temp = writing and 0b01111111
            writing = writing ushr 7
            if (writing != 0L)
                temp = temp or 0b10000000
            outputStream.writeByte(temp.toInt())
        } while (writing != 0L)
    }

    fun writeFloat(value: Float) {
        outputStream.writeFloat(value)
    }

    fun writeDouble(value: Double) {
        outputStream.writeDouble(value)
    }

    fun writeString(value: String) {
        val strBytes = value.toByteArray()
        val strLength = strBytes.size
        this.writeVarInt(strLength)
        for (i in 0 until strLength)
            outputStream.writeByte(strBytes[i].toInt())
    }

    fun writePosition(value: BlockPosition) {
        val encoded: Long = ((value.x and 0x3FFFFFF).toLong() shl 38) or ((value.x and 0x3FFFFFF).toLong() shl 12) or (value.y and 0xFFF).toLong()
        outputStream.writeLong(encoded)
    }

    fun writeUUID(value: UUID) {
        outputStream.writeLong(value.mostSignificantBits)
        outputStream.writeLong(value.leastSignificantBits)
    }

    override fun close() {
        outputStream.close()
    }

    internal companion object {
        fun writeVarInt(value: Int, outputStream: FilterOutputStream) {
            var writing = value
            do {
                var temp = writing and 0b01111111
                writing = writing ushr 7
                if (writing != 0)
                    temp = temp or 0b10000000
                outputStream.write(temp)
            } while (writing != 0)
        }
    }

}
