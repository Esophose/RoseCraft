package dev.rosewood.rosecraft.network.netty.cipher

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import javax.crypto.Cipher

class CipherBase(private val cipher: Cipher) {

    private var heapIn = ByteArray(0)
    private var heapOut = ByteArray(0)

    private fun bufToByte(buffer: ByteBuf): ByteArray {
        val length = buffer.readableBytes()
        if (heapIn.size < length)
            heapIn = ByteArray(length)
        buffer.readBytes(heapIn, 0, length)
        return heapIn
    }

    fun decipher(channel: ChannelHandlerContext, buffer: ByteBuf): ByteBuf {
        val length = buffer.readableBytes()
        val bytes = bufToByte(buffer)
        val outputBuffer = channel.alloc().heapBuffer(cipher.getOutputSize(length))
        outputBuffer.writerIndex(cipher.update(bytes, 0, length, outputBuffer.array(), outputBuffer.arrayOffset()))
        return outputBuffer
    }

    fun encipher(buffer: ByteBuf, outputBuffer: ByteBuf) {
        val length = buffer.readableBytes()
        val bytes = bufToByte(buffer)
        val outputLength = cipher.getOutputSize(length)
        if (heapOut.size < outputLength)
            heapOut = ByteArray(outputLength)
        outputBuffer.writeBytes(heapOut, 0, cipher.update(bytes, 0, length, heapOut))
    }

}
