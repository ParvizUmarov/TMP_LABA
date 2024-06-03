package org.example.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.message.LoginResponseListener;
import org.example.message.Message;
import org.example.message.ResponseListener;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;
import org.example.transport.Transport;

import java.io.IOException;

public class LoginController {
    private Transport transport;
    public TextField username;
    public TextField password;
    public Button signInBtn;

    public AnchorPane rootNode;

    public void setTransport(Transport transport) {
        this.transport = transport;
        this.responseListener = new LoginResponseListener(transport, this);
    }

    private LoginResponseListener responseListener;

    public void onBtnPressed(ActionEvent actionEvent) throws IOException {
        var user = username.getText();
        var pass = password.getText();

        transport.send(new LoginRequest(user, pass));

        System.out.println("username: " + username.getText());
        System.out.println("password: " + password.getText());

        onLoginSuccess();

//
//        Thread receiveThread = new Thread(() -> {
//            System.out.println("listen message from server");
//            Message msg = transport.receiveMessage();
//            System.out.println("receive message");
//
//            if (msg instanceof LoginResponse) {
//                System.out.println("login response: " + ((LoginResponse) msg).isLoginSuccess());
//
//                Platform.runLater(() -> {
//                    try {
//                        if(((LoginResponse) msg).isLoginSuccess()){
//                            onLoginSuccess();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        });
//
//        receiveThread.start();

//        System.out.println("listen message from server");
//        var msg = transport.receiveMessage();
//        System.out.println("receive message");
//
//        if(msg instanceof LoginResponse){
//            System.out.println("login response: " + ((LoginResponse) msg).isLoginSuccess());
//        }
    }

    private void onLoginSuccess() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Chat");
        stage.show();

        ChatController chatController = loader.getController();
        chatController.setTransport(transport);

    }


//    {
//        var t = new Thread(() -> {
//            while (true) {
//                try {
//                    var msg = transport.receiveMessage();
//                    if(msg instanceof LoginResponse){
//                        System.out.println("Login response received message");
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
