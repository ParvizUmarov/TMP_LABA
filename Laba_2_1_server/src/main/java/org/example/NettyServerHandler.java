package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.message.Message;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
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
        ByteBuf byteBuf = (ByteBuf) msg;

        if (byteBuf.readableBytes() < 4) {
            ctx.fireChannelReadComplete();
            return;
        }

        int byteArrayLength = (int) byteBuf.readUnsignedInt();
        System.out.println(byteArrayLength);

        if (byteBuf.readableBytes() < byteArrayLength) {
            ctx.fireChannelReadComplete();
            return;
        }

        if(byteArrayLength > 0){
            byte[] byteArray = new byte[byteArrayLength];
            byteBuf.readBytes(byteArray);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Message message = (Message) objectInputStream.readObject();

            if (message instanceof LoginRequest) {
                LoginRequest loginRequest = (LoginRequest) message;
                System.out.println("Received username: " + loginRequest.getUsername());
                System.out.println("Received password: " + loginRequest.getPassword());

                // Сохранить ответ в переменной, чтобы отправить его в методе channelReadComplete()
                Message response = new LoginResponse(true);
                ctx.writeAndFlush(response);
                System.out.println("send response: " + response.getClass());
                //ctx.channel().attr(ResponseAttrKey.INSTANCE).set(response);
            }

            ctx.fireChannelReadComplete();
        }
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
