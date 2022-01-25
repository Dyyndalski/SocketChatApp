package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class WriteFiles {

    private String nickName;
    private DateTimeFormatter formatter;

    public WriteFiles(String nickName) {
        this.nickName = nickName;
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH;mm;ss");
    }

    public ArrayList<Holder> readFromFile(File file, String type) throws FileNotFoundException {
        ArrayList<Holder> sendMessages = new ArrayList<Holder>();
        Scanner myReader = new Scanner(file);

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                String[] help = line.split(":");
                sendMessages.add(new Holder(help[0], LocalDateTime.parse(help[1], formatter), help[2], type));
            }
        }
        myReader.close();
        return sendMessages;
    }

    public ArrayList<Holder> getMessages() throws FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH;mm;ss");
        File sendFile = new File("src/main/java/messages/S" + this.nickName + ".txt");
        File receiveFile = new File("src/main/java/messages/R" + this.nickName + ".txt");

        ArrayList<Holder> sendMessages = new ArrayList<Holder>();
        ArrayList<Holder> receiveMessages = new ArrayList<Holder>();

        sendMessages = readFromFile(sendFile, "SEND");
        receiveMessages = readFromFile(receiveFile, "RECEIVE");

        sendMessages.addAll(receiveMessages);
        sendMessages.sort(new Sorting());

        return sendMessages;
    }
}