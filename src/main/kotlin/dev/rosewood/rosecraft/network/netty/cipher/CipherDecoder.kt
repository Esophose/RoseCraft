package dev.rosewood.rosecraft.network.netty.cipher

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import javax.crypto.Cipher

class CipherDecoder(cipher: Cipher) : MessageToMessageDecoder<ByteBuf>() {

    private val cipher = CipherBase(cipher)

    override fun decode(channel: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        out.add(cipher.decipher(channel, buffer))
    }

}
