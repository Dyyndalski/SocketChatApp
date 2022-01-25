package com.example.demo1;

import client.Client;
import client.ReceiveMessageHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ApplicationModel;

import java.io.IOException;
import java.net.Socket;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StartWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Socket Chat Application!");
        stage.setScene(scene);
        stage.show();

    }

    private void initApp() throws IOException {

        ApplicationModel.getInstance();


    }

    public static void main(String[] args) {
        launch();
    }
}