package model;

import client.Client;
import client.WriteFiles;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<Message> messages;
    private WriteFiles writeFiles;


    public User(String username, ArrayList<Message> messages) {
        this.username = username;
        this.messages = messages;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
