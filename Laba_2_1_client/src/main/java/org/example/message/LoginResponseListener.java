package org.example.message;

import org.example.controller.LoginController;
import org.example.message.Message;
import org.example.message.ResponseListener;
import org.example.message.storage.LoginResponse;
import org.example.transport.Transport;

import java.io.IOException;

public class LoginResponseListener implements ResponseListener, Runnable {

    private Transport transport;
    private LoginController controller;

    public LoginResponseListener(Transport transport, LoginController controller) {
        this.transport = transport;
        this.controller = controller;
    }

    @Override
    public void run() {
        Message msg = null;
        while (msg == null) {
            try {
                msg = transport.receiveMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (msg != null && msg instanceof LoginResponse) {
//                try {
//                   // controller.onLoginResponseReceived((LoginResponse) msg);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }

    @Override
    public void onResponseReceived(Message message) throws IOException {
        if (message instanceof LoginResponse) {
           // controller.onLoginResponseReceived((LoginResponse) message);
        }
    }

    @Override
    public void onError(Throwable error) {

    }
}