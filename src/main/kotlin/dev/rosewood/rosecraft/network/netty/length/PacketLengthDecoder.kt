package dev.rosewood.rosecraft.network.netty.length

import dev.rosewood.rosecraft.network.packet.PacketUtils
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException

class PacketLengthDecoder : ByteToMessageDecoder() {

    override fun decode(channel: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        buffer.markReaderIndex()
        val bytes = ByteArray(3)
        for (i in bytes.indices) {
            if (!buffer.isReadable) {
                buffer.resetReaderIndex()
                return
            }

            bytes[i] = buffer.readByte()
            if (bytes[i] < 0)
                continue

            val bytesBuffer = Unpooled.wrappedBuffer(bytes)
            val packetLength = PacketUtils.readVarInt(bytesBuffer)
            bytesBuffer.release()
            if (buffer.readableBytes() < packetLength) {
                buffer.resetReaderIndex()
                return
            }

            out.add(buffer.readBytes(packetLength))
            return
        }
        throw CorruptedFrameException("length wider than 21-bit")
    }

}
