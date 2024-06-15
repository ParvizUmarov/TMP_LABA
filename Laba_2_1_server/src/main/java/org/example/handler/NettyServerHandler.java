package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.net.Socket;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.example.entity.MessageEntity;
import org.example.entity.User;
import org.example.message.Message;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;
import org.example.message.storage.SendMessageRequest;
import org.example.message.storage.SendMessageResponse;
import org.example.repository.MessageRepo;
import org.example.repository.UserRepo;

import java.util.concurrent.CopyOnWriteArrayList;
import io.netty.util.internal.logging.InternalLoggerFactory;

import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final CopyOnWriteArrayList<Channel> channels;
    private String clientName;
    private static int newClientIndex = 1;
    private UserRepo userRepo;
    private MessageRepo messageRepo;

    public NettyServerHandler(CopyOnWriteArrayList<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
        System.out.println("Client connected: " + ctx);
        channels.add(ctx.channel());
        ctx.writeAndFlush("CONNECTED");
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
                var username = loginRequest.getUsername();
                var password = loginRequest.getPassword();

                System.out.println("Received message: username=" + loginRequest.getUsername()+",password="+loginRequest.getPassword());

                userRepo = new UserRepo();
                var isUserExist = userRepo.getUserFromDb(new User(username, password));
                var resMsg = "user <" + username +"> is successfully authorized";

                if(isUserExist){
                    var response = new LoginResponse(true);
                    ctx.channel().writeAndFlush(response);
                    System.out.println(resMsg);
                    System.out.println("Send response: " + response.getClass().getName());
                }else{
                    var response = new LoginResponse(false);
                    ctx.channel().writeAndFlush(response);
                    System.out.println("user<"+username+"> entered the wrong username or password");
                }
            }else if(message instanceof SendMessageRequest){
                var sendMessageRequest = (SendMessageRequest) message;

                var username = sendMessageRequest.getUsername();
                var msgInfo = sendMessageRequest.getMessage();
                var time = sendMessageRequest.getTime();
                
                messageRepo = new MessageRepo();
                messageRepo.createMessage(new MessageEntity(username, msgInfo, time));

                System.out.println("get all messages: " + messageRepo.getAllMessages());

                var response = new SendMessageResponse(username, msgInfo, time);
                //ctx.channel().writeAndFlush(response);

                for(var ch : channels){
                    ch.writeAndFlush(response);
                    System.out.println("send message: " + response + " - to client >> " + ch);
                }
            }
            ctx.fireChannelReadComplete();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();

    }

}
