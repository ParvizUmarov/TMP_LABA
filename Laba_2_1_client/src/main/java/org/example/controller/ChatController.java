package org.example.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.message.LoginResponseListener;
import org.example.message.Message;
import org.example.message.storage.LoginResponse;
import org.example.message.storage.SendMessageRequest;
import org.example.message.storage.SendMessageResponse;
import org.example.transport.Transport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatController {

    private Transport transport;
    public TextArea area;
    public TextField message;

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        System.out.println("send message: " + message.getText());

        var username = "parviz";
        var msg = message.getText();
        var time = LocalDateTime.now();

        transport.send(new SendMessageRequest(username, msg, time));
        System.out.println("send message");

//        var msg1 = transport.receiveMessage();
//        if (msg1 instanceof LoginResponse) {
//            System.out.println("Login response received message");
//        }

        var messageInfo = username + "\n" + message.getText() + "\n" + time.format(DateTimeFormatter.ofPattern("HH:mm")) + "\n";

        area.appendText(messageInfo + "\n");
        message.clear();

//        Thread receiveThread = new Thread(() -> {
//            System.out.println("listen message from server");
//            Message msg1 = transport.receiveMessage();
//            System.out.println("receive message");
//
//            if (msg1 instanceof SendMessageResponse) {
//                System.out.println("login response: " + ((SendMessageResponse) msg1).getMessage());
//
//            }
//        });
//
//        receiveThread.start();
    }

//    {
//        var t = new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(2000);
//                    var msg = transport.receiveMessage();
//                    if (msg instanceof SendMessageResponse) {
//                        System.out.println("login response: " + ((SendMessageResponse) msg).getMessage());
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//    }


}
