package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;

public class OutPutHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("Channel write message: " + msg);

        if (msg instanceof LoginRequest) {
            LoginRequest loginRequest = (LoginRequest) msg;
            System.out.println("Received username: " + loginRequest.getUsername());
            System.out.println("Received password: " + loginRequest.getPassword());

            ctx.writeAndFlush("READY");

            var response = new LoginResponse(true);
            ctx.writeAndFlush(response);
            System.out.println("Sent response: " + response.getClass());
        }

        ctx.fireChannelReadComplete();

        ctx.write(msg, promise); // This will pass the message to the next handler in the pipeline
    }
}
