package org.example.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.message.Message;
import org.example.message.storage.SendMessageRequest;
import org.example.message.storage.SendMessageResponse;
import org.example.transport.Transport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatController {

    public ScrollPane sp_main;
    public VBox vbox_messages;
    private Transport transport;
    public TextField message;
    private Task<Void> receiveTask;
    private String userName;

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void initialize() {
        startReceiveTask();
    }

    private void startReceiveTask(){
        receiveTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    //Thread.sleep(1000);
                    Message msg = transport.receiveMessage();
                    if (msg instanceof SendMessageResponse) {
                        Platform.runLater(() -> {
                            System.out.println("Login response received message");
                            vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    sp_main.setVvalue((Double) newValue);
                                }
                            });

                            VBox vBox = new VBox();
                            vBox.setPadding(new Insets(5,5,5,10));

                            var username = ((SendMessageResponse) msg).getUsername();
                            var message = ((SendMessageResponse) msg).getMessage();
                            var time = ((SendMessageResponse) msg).getTime().format(DateTimeFormatter.ofPattern("HH:mm"));

                            Text usernameText = new Text(username);
                            usernameText.setFill(Color.GREY);

                            Text messageText = new Text(message);

                            Text timeText = new Text(time);
                            timeText.setFill(Color.GREY);

                            TextFlow textFlow = new TextFlow(messageText);
                            textFlow.setPadding(new Insets(5,10,5,10));

                            HBox messageBox = new HBox(textFlow);

                            if(userName.equals(username)){
                                vBox.setAlignment(Pos.CENTER_RIGHT);
                                messageBox.setAlignment(Pos.CENTER_RIGHT);
                                messageText.setFill(Color.color(0.934, 0.945, 0.996));
                                textFlow.setStyle("-fx-color: rgb(239, 242, 255); -fx-background-color: rgb(15, 125, 242); -fx-background-radius: 20px");
                            }else{
                                vBox.setAlignment(Pos.CENTER_LEFT);
                                messageBox.setAlignment(Pos.CENTER_LEFT);
                                messageText.setFill(Color.BLACK);
                                textFlow.setStyle("-fx-color: black; -fx-background-color: rgb(192, 192, 192); -fx-background-radius: 20px");
                            }

                            vBox.getChildren().addAll(usernameText, messageBox, timeText);
                            vbox_messages.getChildren().add(vBox);
                        });
                    }
                }
            }
        };
        new Thread(receiveTask).start();
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        System.out.println("send message: " + message.getText());

        var msg = message.getText();
        var time = LocalDateTime.now();

        transport.send(new SendMessageRequest(userName, msg, time));
        System.out.println("send message");
        message.clear();
    }
}
