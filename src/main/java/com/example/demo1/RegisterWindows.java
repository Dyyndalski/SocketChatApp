package com.example.demo1;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import model.ApplicationModel;

import java.io.IOException;

public class RegisterWindows {

    @FXML
    private Button onClickRegister;

    @FXML
    private TextField onInputLogin;

    @FXML
    private TextField onInputPass;

    @FXML
    void onInputPass(InputMethodEvent event) {
        onClickRegister.setText(event.getCommitted());
    }

    ApplicationModel applicationModelInstance;

    @FXML
    void OnClickRegister() throws IOException, InterruptedException {
        String login = onInputLogin.getText();
        String password = onInputPass.getText();

        applicationModelInstance.setClient(new Client("192.168.0.11", 8888));
        applicationModelInstance.getClient().run();
        applicationModelInstance.getClient().login(login);
        applicationModelInstance.setMyNick(password);


    }

    @FXML
    void onInputLogin(InputMethodEvent event) {
        onInputLogin.setText(event.getCommitted());
    }
}
