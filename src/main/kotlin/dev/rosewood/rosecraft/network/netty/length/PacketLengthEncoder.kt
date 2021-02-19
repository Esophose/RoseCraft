package dev.rosewood.rosecraft.network.netty.length

import dev.rosewood.rosecraft.network.packet.PacketUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class PacketLengthEncoder : MessageToByteEncoder<ByteBuf>() {

    override fun encode(channel: ChannelHandlerContext, buffer: ByteBuf, outputBuffer: ByteBuf) {
        val length = buffer.readableBytes()
        val varIntPacketLength = PacketUtils.lengthVarInt(length)
        if (varIntPacketLength > 3)
            error("Unable to send packet, too large")

        PacketUtils.ensureWritable(outputBuffer, length + varIntPacketLength)
        PacketUtils.writeVarInt(outputBuffer, length)
        outputBuffer.writeBytes(buffer, buffer.readerIndex(), length)
    }

}
