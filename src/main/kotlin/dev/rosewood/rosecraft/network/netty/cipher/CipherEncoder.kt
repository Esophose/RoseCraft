package dev.rosewood.rosecraft.network.netty.cipher

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import javax.crypto.Cipher

class CipherEncoder(cipher: Cipher) : MessageToByteEncoder<ByteBuf>() {

    private val cipher = CipherBase(cipher)

    override fun encode(channel: ChannelHandlerContext, buffer: ByteBuf, outputBuffer: ByteBuf) {
        cipher.encipher(buffer, outputBuffer)
    }

}
