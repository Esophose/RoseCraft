package dev.rosewood.rosecraft.network.netty

import dev.rosewood.rosecraft.network.netty.channel.ClientChannel
import dev.rosewood.rosecraft.network.netty.length.PacketLengthDecoder
import dev.rosewood.rosecraft.network.netty.length.PacketLengthEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.InetSocketAddress

class NettyServer {

    private val group = NioEventLoopGroup()

    var address = "127.0.0.1"
    var port = 25565

    fun start(address: String = this.address, port: Int = this.port) {
        val serverBootstrap = ServerBootstrap()
        serverBootstrap.group(group)
        serverBootstrap.channel(NioServerSocketChannel::class.java)
        serverBootstrap.localAddress(InetSocketAddress(address, port))

        serverBootstrap.childHandler(object: ChannelInitializer<SocketChannel>() {
            override fun initChannel(channel: SocketChannel) {
                channel.pipeline().addLast("splitter", PacketLengthDecoder())
                channel.pipeline().addLast("packet_encoder", ClientChannel())
                channel.pipeline().addLast("prepender", PacketLengthEncoder())
            }
        })

        val channelFuture = serverBootstrap.bind().sync()

        if (!channelFuture.isSuccess)
            error("Unable to bind server at $address:$port")
    }

    fun stop() {
        group.shutdownGracefully().sync()
    }

}
