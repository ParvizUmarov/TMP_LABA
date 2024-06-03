package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.example.entity.MessageEntity;
import org.example.entity.User;
import org.example.message.Message;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;
import org.example.message.storage.SendMessageRequest;
import org.example.message.storage.SendMessageResponse;
import org.example.repository.MessageRepo;
import org.example.repository.UserRepo;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;
    private UserRepo userRepo;
    private MessageRepo messageRepo;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
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
                var userName = loginRequest.getUsername();
                var password = loginRequest.getPassword();

                System.out.println("Received username: " + loginRequest.getUsername());
                System.out.println("Received password: " + loginRequest.getPassword());

                userRepo = new UserRepo();
                var isUserExist = userRepo.getUserFromDb(new User(userName, password));

                if(isUserExist){
                    System.out.println("successfully authorization");
                }else{
                    System.out.println("you entered the wrong username or password");
                }

                var response = new LoginResponse(true);
                ctx.writeAndFlush(response);
                System.out.println("Sent response: " + response.getClass().getName());
            }else if(message instanceof SendMessageRequest){
                var sendMessageRequest = (SendMessageRequest) message;

                var username = sendMessageRequest.getUsername();
                var msgInfo = sendMessageRequest.getMessage();
                var time = sendMessageRequest.getTime();
                
                messageRepo = new MessageRepo();
                messageRepo.createMessage(new MessageEntity(username, msgInfo, time));

                System.out.println("get all messages: " + messageRepo.getAllMessages());

                var response = new SendMessageResponse(username, msgInfo, time);
                ctx.writeAndFlush(response);
                System.out.println("send message: " + response + " - to client");

            }

            ctx.fireChannelReadComplete();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();

    }

}
