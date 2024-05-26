package com.example.laba_2_1_client_;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Network network;

    @FXML
    TextField messageField;

    @FXML
    TextArea area;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network();
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        network.sendMessage(messageField.getText());
        messageField.clear();
        messageField.requestFocus();
    }
}