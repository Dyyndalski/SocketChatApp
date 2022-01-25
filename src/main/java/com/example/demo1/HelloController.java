package com.example.demo1;

import client.Client;
import client.Holder;
import client.ReceiveMessageHandler;
import client.WriteFiles;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ApplicationModel;
import model.Type;
import model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HelloController implements Initializable {

    static HelloController instance;
    public static HelloController getInstance(){
        if(instance == null){
            instance = new HelloController();
        }
        return instance;
    }


    @FXML
    TextField textFieldId;
    @FXML
    Button submitBtn;
    @FXML
    TextArea responseMess;
//    @FXML
//    VBox availableUsersMenu;
    @FXML
    ScrollPane scrollPaneId;
    @FXML
    Pane xxx;
    @FXML VBox vBoxId;
    @FXML VBox vBoxMess;
    @FXML Button refreshBtn;
    @FXML Label userNickName;

    private String currentUser = "";
    private String usr = "";
    private Integer userListSize = 0;
    ArrayList<Button> buttons = new ArrayList<>();


    ApplicationModel applicationModelInstance;
//    ReceiveMessageHandler receiveMessageInstance;

    @FXML void onChangeInputField(InputMethodEvent event){
        textFieldId.setText(event.getCommitted());
    }


    @FXML void refresh(){
        System.out.println("=== added users ===");
        System.out.println(applicationModelInstance.getAddedUsers());
        refreshAll();
    }
    @FXML
    public void reloadWindowMessage(WriteFiles wf, String user) throws FileNotFoundException {
        vBoxMess.getChildren().clear();

        List<Holder> filteredMessages = new ArrayList<>();
        filteredMessages = wf.getMessages().stream().filter( holder -> {
            return holder.getPerson().equals(user);
        }).collect(Collectors.toList());

        filteredMessages.stream().forEach( holder -> {

            HBox hBox = new HBox();
            Label label = new Label();



            Insets insets = new Insets(2, 2, 2, 2);
            hBox.setMargin(label, insets);
            label.setText(holder.getMessage());
            label.setStyle("-fx-border-color: gray;" + "-fx-text-fill: black;" + "-fx-font-size: 25;" + "-fx-font-style: black; "+ "-fx-border-color: SILVER;" +
                    "-fx-border-radius: 10;" + "-fx-background-color: GHOSTWHITE;" + "-fx-background-radius: 10;"
            );

            label.setAlignment(Pos.CENTER);
            hBox.getChildren().add(label);
            if(holder.getType().equals("SEND")){
                hBox.setAlignment(Pos.BASELINE_RIGHT);
            }else {
                hBox.setAlignment(Pos.BASELINE_LEFT);
            }
            vBoxMess.getChildren().add(hBox);
        });
    }

    public void refreshAll(){
        vBoxId.getChildren().clear();
        if(!applicationModelInstance.getMyNick().isEmpty()){
            userNickName.setText("Zalogowano jako: " + applicationModelInstance.getMyNick());
            userNickName.setAlignment(Pos.CENTER);
        }
        applicationModelInstance.getAddedUsers().stream().forEach(user -> {

            Button btn = new Button(user);
            btn.setStyle(
                    "-fx-font-size: 18;" +
                    "-fx-text-fill: black;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 1;");
            btn.setPrefWidth(vBoxId.getPrefWidth());
            btn.setPrefHeight(50);
            btn.setId(user);
            btn.setTextAlignment(TextAlignment.CENTER);
            buttons.add(btn);
            System.out.println("this is user ho send mess : " + user);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            currentUser = user;


                            vBoxMess.getChildren().clear();

                            System.out.println("==========");
                            System.out.println(currentUser);
                            System.out.println("==========");
                            WriteFiles wf = new WriteFiles(applicationModelInstance.getMyNick());
                            applicationModelInstance.setWf(wf);
                            List<Holder> filteredMessages = new ArrayList<>();
                            try {
                                filteredMessages = wf.getMessages().stream().filter( holder -> {
                                    return holder.getPerson().equals(user);
                                }).collect(Collectors.toList());
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }

                            filteredMessages.stream().forEach( holder -> {
                                HBox hBox = new HBox();
                                Label label = new Label();

                                Insets insets = new Insets(2, 2, 2, 2);
                                hBox.setMargin(label, insets);
                                label.setText(holder.getMessage());
                                label.setStyle("-fx-border-color: gray;" + "-fx-text-fill: black;" + "-fx-font-size: 25;" + "-fx-font-style: black; "+ "-fx-border-color: SILVER;" +
                                          "-fx-border-radius: 10;" + "-fx-background-color: GHOSTWHITE;" + "-fx-background-radius: 10;"
                                        );
                                label.setAlignment(Pos.CENTER);
                                hBox.getChildren().add(label);
                                if(holder.getType().equals("SEND")){
                                    hBox.setAlignment(Pos.BASELINE_RIGHT);
                                }else {
                                    hBox.setAlignment(Pos.BASELINE_LEFT);
                                }
                                vBoxMess.getChildren().add(hBox);
                            });

                            submitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                    new EventHandler<MouseEvent>() {
                                        @Override public void handle(MouseEvent e) {
                                            System.out.println("SEND BUTTON CLICKED");
                                            System.out.println("SENT TO"  + currentUser);
                                            try {
                                                applicationModelInstance.getClient().toWhom(currentUser, textFieldId.getText());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                            textFieldId.clear();

                                            new AnimationTimer() {
                                                @Override
                                                public void handle(long l) {
                                                    try {
                                                        if(!currentUser.equals(user)){
                                                            stop();
                                                            reloadWindowMessage(applicationModelInstance.getWf(), user);
                                                            ;
                                                        }
                                                        reloadWindowMessage(applicationModelInstance.getWf(), user);
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        }
                                    });
                        }
                    });
//            btn.setOnAction();

            vBoxId.getChildren().add(btn);
        });
    }
    public void hello(){
        System.out.println("HELLO DZIALAAA");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        applicationModelInstance = ApplicationModel.getInstance();


        vBoxId.getChildren().clear();

//        new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                if(applicationModelInstance.getAddedUsers().size() != 0){
//                    stop();
//
//                }
//                System.out.println("size : " + applicationModelInstance.getAddedUsers().size());
//            }
//        }.start();

        refreshAll();


    }
}