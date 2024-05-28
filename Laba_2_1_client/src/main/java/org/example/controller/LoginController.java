package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.message.storage.LoginRequest;
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
    }

    public void onBtnPressed(ActionEvent actionEvent) throws IOException {
        var user = username.getText();
        var pass = password.getText();

        transport.send(new LoginRequest(user, pass));

        System.out.println("username: " + username.getText());
        System.out.println("password: " + password.getText());

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/chat-view.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Chat");
        stage.show();
    }
}
