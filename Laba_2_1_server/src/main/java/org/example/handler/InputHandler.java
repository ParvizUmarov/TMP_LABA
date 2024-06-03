package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.message.Message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class InputHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel read input message: " + msg.getClass());

        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() < 4) {
            ctx.fireChannelReadComplete();
            return;
        }

        int byteArrayLength = (int) byteBuf.readUnsignedInt();

        if (byteBuf.readableBytes() < byteArrayLength) {
            ctx.fireChannelReadComplete();
            return;
        }

        if (byteArrayLength > 0) {
            byte[] byteArray = new byte[byteArrayLength];
            byteBuf.readBytes(byteArray);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Message message = (Message) objectInputStream.readObject();

            ctx.fireChannelRead(message);
            ctx.fireChannelReadComplete();
            System.out.println("type class: " + message.getClass());
            System.out.println("send to outPutHandler");

        }
    }
}
