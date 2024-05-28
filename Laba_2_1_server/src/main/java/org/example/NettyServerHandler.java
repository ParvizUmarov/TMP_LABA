package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx);
        channels.add(ctx.channel());
        clientName = "Client #" + newClientIndex;
        newClientIndex++;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client #" + newClientIndex + "is exit");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    //    @Override
//    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println("Receive message: " + msg);
//        String out = String.format("[%s]: %s\n", clientName, msg);
//        ctx.writeAndFlush(out);
////        for(Channel c : channels){
////            c.writeAndFlush(out);
////        }
//
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();

    }

}
