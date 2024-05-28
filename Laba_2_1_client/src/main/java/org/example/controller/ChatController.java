package org.example.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {


    public TextArea area;
    public TextField message;

    public void sendMessageAction(ActionEvent actionEvent) {
        System.out.println("send message: " + message.getText());
        area.appendText(message.getText() + "\n");
        message.clear();
    }
}
