package org.example;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.example.handler.NettyServerHandler;
import org.example.handler.ObjectWriterHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public class NettyServer {
    private static final CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();

    public static void main(String[] args){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                                channels.add(ch);
                                ch.pipeline().addLast(new NettyServerHandler(channels));
                                ch.pipeline().addLast(new ObjectWriterHandler());
                                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });

            ChannelFuture future = b.bind(Settings.PORT).sync();
            future.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
