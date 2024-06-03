package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.ChatController;
import org.example.controller.LoginController;
import org.example.transport.Transport;

public class App extends Application {
    private Transport transport;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        transport = new Transport();
        Thread transportThread = new Thread(transport::connect);
        transportThread.setDaemon(true);
        transportThread.start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Chat App");
        stage.centerOnScreen();
        stage.show();

        LoginController controller = loader.getController();
        controller.setTransport(transport);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        transport.disconnect();
    }
}
