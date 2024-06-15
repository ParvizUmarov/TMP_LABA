package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class ObjectWriterHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        System.out.println("msg " + msg.getClass());
        System.out.println("byte len: " + bytes.length);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        ctx.writeAndFlush(buffer);

    }

}
