package dev.rosewood.rosecraft.network.netty.channel

import dev.rosewood.rosecraft.network.PacketProcessor
import dev.rosewood.rosecraft.network.netty.cipher.CipherDecoder
import dev.rosewood.rosecraft.network.netty.cipher.CipherEncoder
import dev.rosewood.rosecraft.network.netty.cipher.CipherUtils
import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.PacketUtils
import dev.rosewood.rosecraft.players.PlayerList
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import javax.crypto.Cipher
import javax.crypto.SecretKey

class ClientChannel : SimpleChannelInboundHandler<ByteBuf>() {

    private var encrypted = false
    private lateinit var context: ChannelHandlerContext

    override fun channelActive(ctx: ChannelHandlerContext) {
        println("Connection established") // TODO
        context = ctx

        PacketProcessor.clientConnections[ctx] = this
    }

    override fun channelRead0(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        if (buffer.readableBytes() == 0)
            return

        val id = PacketUtils.readVarInt(buffer)
        val reader = PacketReader(buffer)
        PacketProcessor.process(ctx, id, reader)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("Disconnected") // TODO

        val playerConnection = PacketProcessor.playerConnections[ctx] ?: return

        playerConnection.online = false
        PlayerList.remove(playerConnection)

        PacketProcessor.playerConnections.remove(ctx)
        PacketProcessor.clientConnections.remove(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close() // TODO: Do we want to close if we get an invalid packet?
    }

    fun setEncryptionKey(secretKey: SecretKey) {
        context.pipeline().addBefore("splitter", "decrypt", CipherDecoder(CipherUtils.getCipher(Cipher.DECRYPT_MODE, secretKey)))
        context.pipeline().addBefore("packet_encoder", "encrypt", CipherEncoder(CipherUtils.getCipher(Cipher.ENCRYPT_MODE, secretKey)))
        encrypted = true
    }

}
