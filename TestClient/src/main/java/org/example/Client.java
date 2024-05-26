package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.initializer.ClientAdapterInitializer;

public class Client {
    private static final IO io = new IO();

    public static void main(String[] args) throws Exception {
        new Client().start();
    }

    public void start(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientAdapterInitializer());

            ChannelFuture channelFuture = bootstrap.connect(Settings.HOST, Settings.PORT).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
