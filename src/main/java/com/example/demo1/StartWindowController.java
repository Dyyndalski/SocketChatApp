package com.example.demo1;

import client.Client;
import client.ReceiveMessageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ApplicationModel;
import model.Message;
import model.Type;
import model.User;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;


public class StartWindowController implements Initializable {

    @FXML
    Button submitNickBtn;
    @FXML
    TextField inputNickField;
    @FXML
    AnchorPane startWindowId;
    @FXML
    Button registration;

    ApplicationModel applicationModelInstance;

    @FXML
    public void onSubmitNick() throws IOException, InterruptedException {
        System.out.println(inputNickField.getText());
        String text = inputNickField.getText();
        if(!text.isEmpty()){

            applicationModelInstance.setClient(new Client("192.168.0.11", 8888));
            applicationModelInstance.getClient().run();
            applicationModelInstance.getClient().login(text);
            applicationModelInstance.setMyNick(text);


            User newUser = new User(text, new ArrayList<>());
            applicationModelInstance.getUsers().add(newUser);
            Parent helloView = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage window = (Stage) submitNickBtn.getScene().getWindow();
            window.setScene(new Scene(helloView));
        }
    }

    @FXML
    public void inRegisterClick() throws IOException {
        Parent registerView = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Stage window = (Stage) submitNickBtn.getScene().getWindow();
        window.setScene(new Scene(registerView));
    }

    @FXML
    public void onInputNickField(InputMethodEvent event){
        inputNickField.setText(event.getCommitted());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applicationModelInstance = ApplicationModel.getInstance();
        ArrayList<User> tempUsers = new ArrayList<>();
        ArrayList<Message> jMess = new ArrayList<>();
        jMess.add(new Message("siemanko tu janek", Type.SENT));
        jMess.add(new Message("co tam u ciebie janek?", Type.RECV));
        ArrayList<Message> kMess = new ArrayList<>();
        kMess.add(new Message("siemanko tu kacper", Type.SENT));
        kMess.add(new Message("co tam u ciebie kacper?", Type.RECV));
        tempUsers.add(new User("Janek", jMess));
        tempUsers.add(new User("Kacpeeeer", kMess));
        applicationModelInstance.setUsers(tempUsers);
    }


}
