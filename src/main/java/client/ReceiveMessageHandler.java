package client;

import model.ApplicationModel;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ReceiveMessageHandler implements Runnable {

    private Socket client;
    private int counter;
    private  boolean flag;
    private int length;
    private String from;
    private String nickname;
    private PrintWriter printWriter;
    private File file;
    private LocalDateTime dateTime;
    Scanner inputScanner;
    DateTimeFormatter formatter;
    String allMessage;
    String message;
    String[] messageToMe;
    String toWhom;
    private ArrayList<String> users = new ArrayList<>();
    private BufferedWriter writer;


//    static ReceiveMessageHandler instance;
//    public static ReceiveMessageHandler getInstance(Socket client, String nickname){
//        if(instance == null){
//            instance = new ReceiveMessageHandler(client, nickname);
//        }
//        return instance;
//    }

    ApplicationModel applicationModelInstance;



    public ReceiveMessageHandler(Socket client, String nickname) {
        this.client = client;
        this.flag = false;
        this.nickname = nickname;
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH;mm;ss");

    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    File createFile(String nickName) throws IOException {
        file = new File("src/main/java/messages/R" + nickName + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public void run() {
        try {
            inputScanner = new Scanner(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            file = createFile(nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }


//        ArrayList<String> users = new ArrayList<String>();
        while (inputScanner.hasNext()) {

            message = inputScanner.next().replaceAll("\0", "");
            message = message.replaceAll("_", "");

            System.out.println("From server" + message + "\n");

            messageToMe = message.split(":");

            if(messageToMe[0].equals("user") && messageToMe.length == 2){
                users.add(messageToMe[1]);
                applicationModelInstance = ApplicationModel.getInstance();
                applicationModelInstance.getAddedUsers().add(messageToMe[1]);
                System.out.println(users.toString());

            }
            if(messageToMe[0].equals("userout")) {
                if(users.size() == 1){
                    users.remove(messageToMe[1]);
                    applicationModelInstance = ApplicationModel.getInstance();
                    applicationModelInstance.getAddedUsers().remove(messageToMe[1]);
                    System.out.println(users.toString());

                }
                for (Iterator<String> it = users.iterator(); it.hasNext(); ) {
                    String next = it.next();
                    if (next.equals(messageToMe[1])) {
                        users.remove(next);
                        applicationModelInstance = ApplicationModel.getInstance();
                        applicationModelInstance.getAddedUsers().remove(messageToMe[1]);
                    }
                }
                System.out.println(users.toString());
            }

            if (messageToMe[0].equals("from")) {
                from = messageToMe[1];
                if (messageToMe.length <= 2) {
                    message = inputScanner.next().replaceAll("_", " ");
                    messageToMe = message.split(":");
                    if (messageToMe[0].equals("to")) {
                        if (messageToMe[1].equals(nickname)) {
                            length = Integer.valueOf(messageToMe[3]);
                            counter += messageToMe[4].length();
                            allMessage = messageToMe[4];
                        } else {
                            continue;
                        }
                    }
                } else {
                    toWhom = messageToMe[4];
                    length = Integer.valueOf(messageToMe[6]);
                    counter += messageToMe[7].length();
                }
                while (counter != length) {
                    message = inputScanner.next().replaceAll("_", " ");
                    counter += message.length();
                    allMessage += message;
                }
                counter = 0;
            }

            if(message.length() >= 2 && !messageToMe[0].equals("user") && !messageToMe[0].equals("userout")) {
                //printWriter.println(from + ":" + LocalDateTime.now().format(formatter) + ":" + allMessage + "\n");
                //printWriter.flush();

                try {
                    writer.write(from + ":" + LocalDateTime.now().format(formatter) + ":" + allMessage + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                helloControllerInstance = HelloController.getInstance();
//                applicationModelInstance = ApplicationModel.getInstance();
//
//                try {
//                    VBox vBox = helloControllerInstance.getVBoxMess();
//                    if(vBox.equals(null)){
//                        System.out.println("vbox is null");
//                    }else{
//                        helloControllerInstance.reloadWindowMessage(applicationModelInstance.getWf(), vBox);
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                System.out.println("Wiadomosc od " + from + " : " + allMessage.toString());

            }
        }
    }
}
