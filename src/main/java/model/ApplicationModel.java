package model;

import client.Client;
import client.WriteFiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationModel {

    private ArrayList<User> users;
    private ArrayList<String> addedUsers = new ArrayList<>();
    private Client client;
    private String myNick;
    private WriteFiles wf;

    public String getMyNick() {
        return myNick;
    }

    public void setMyNick(String myNick) {
        this.myNick = myNick;
    }

    public ApplicationModel(){

    }
    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }

    public ApplicationModel(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    static ApplicationModel instance;
    public static ApplicationModel getInstance(){
        if(instance == null){
            instance = new ApplicationModel();
        }
        return instance;
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public WriteFiles getWf() {
        return wf;
    }

    public void setWf(WriteFiles wf) {
        this.wf = wf;
    }


    public ArrayList<String> getAddedUsers() {
        return addedUsers;
    }

    public void setAddedUsers(ArrayList<String> addedUsers) {
        this.addedUsers = addedUsers;
    }
}
