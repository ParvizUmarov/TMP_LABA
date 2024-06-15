package org.example.controller;

import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entity.MessageEntity;
import org.example.message.Message;
import org.example.message.storage.LoginRequest;
import org.example.message.storage.LoginResponse;
import org.example.message.storage.SendMessageRequest;
import org.example.message.storage.SendMessageResponse;
import org.example.transport.Transport;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoginController {
    public ImageView imageView;
    private Transport transport;
    public TextField username;
    public TextField password;
    public Button signInBtn;
    StackPane buttonContent = new StackPane();
    public AnchorPane rootNode;
    private Task<Void> receiveTask;
    private Stage primaryStage;
    ProgressIndicator loader = new ProgressIndicator();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void initialize(){
    }

    public void onBtnPressed(ActionEvent actionEvent) throws IOException {
        signInBtn.setGraphic(buttonContent);
        var user = username.getText();
        var pass = password.getText();

        transport.send(new LoginRequest(user, pass));
        System.out.println("username: " + username.getText() + ", password: " + password.getText());
        loaderAction();

        loader.setVisible(true);
        signInBtn.setText("");

        receiveTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(1000);
                    Message msg = transport.receiveMessage();
                    if (msg != null && msg instanceof LoginResponse) {
                        Platform.runLater(() -> {
                                System.out.println("login response: " + ((LoginResponse) msg).isLoginSuccess());
                                if (((LoginResponse) msg).isLoginSuccess()) {
                                    onLoginSuccess();
                                } else {
                                    onLoginError();
                                }
                        });
                    } else {
                        System.err.println("Received unexpected message type: " + msg.getClass().getName());
                    }

                }
            }
        };
        new Thread(receiveTask).start();
    }

    private void loaderAction(){
        try{
            loader.setVisible(false);
            buttonContent.getChildren().add(loader);
        }catch (Exception e){}
    }


    private void onLoginSuccess(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
            Parent rootNode = loader.load();
            Scene scene = new Scene(rootNode);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Chat");
            primaryStage.show();
            ChatController chatController = loader.getController();
            chatController.setTransport(transport);
            chatController.setData(username.getText());
            chatController.initialize();
            receiveTask.cancel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void onLoginError(){
        signInBtn.setText("sign in");
        loader.setVisible(false);
        JFXSnackbar snackbar = new JFXSnackbar(rootNode);
        final JFXSnackbar.SnackbarEvent snackbarEvent = new JFXSnackbar.SnackbarEvent(
                new Label("Wrong username or password. Try again") {{
                    setStyle("-fx-text-fill: red;");
                }},
                Duration.seconds(2.5), null);
        snackbar.enqueue(snackbarEvent);
        username.clear();
        password.clear();
    }

}
