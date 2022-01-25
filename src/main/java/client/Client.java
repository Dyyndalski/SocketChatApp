package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client {

    private String host;
    private int port;
    private String nickName;
    private PrintStream output;
    private DateTimeFormatter formatter;
    private PrintWriter printWriter;
    private Scanner scanInput;
    private File file;
    private Socket client;
    private String toWhom;
    private String inputMessage;
    private BufferedWriter writer;

//    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
//        Client client = new Client("192.168.0.11", 8888);
//        client.run();
//    }

//    public Client() throws IOException, InterruptedException {
//        Client client = new Client("192.168.0.11", 8888);
//    }

    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.scanInput = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH;mm;ss");
    }


    File createFile(String nickName) throws IOException {

        file = new File("src/main/java/messages/S" + nickName + ".txt");
        if (file.exists()) {
            return file;
        } else {
            file.createNewFile();
            return file;
        }
    }

    public void login(String nickName) throws IOException {

        new Thread(new ReceiveMessageHandler(client, nickName)).start();

        output.println(nickName);
        file = createFile(nickName);

        writer = new BufferedWriter(new FileWriter(file, true));
        //printWriter = createPrintWriter(file);
    }


    PrintWriter createPrintWriter (File file) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        return printWriter;
    }

//    public boolean checkEnd(String message) throws IOException {
//        if (message.equals("end")) {
//            scanInput.close();
//            output.close();
//            client.close();
//            return false;
//        }else if(message.equals("list")){
//            WriteFiles writeFiles = new WriteFiles(getNickName());
//            System.out.println(writeFiles.getMessages().toString());
//        }
//        return true;
//    }

    public void run () throws UnknownHostException, IOException, InterruptedException {
        client = new Socket(host, port);
        output = new PrintStream(client.getOutputStream());
    }

    public void toWhom(String toWhom, String inputMessage) throws IOException {
            if(inputMessage.length() >= 1) {
                writer.write(toWhom + ":" + LocalDateTime.now().format(formatter) + ":" + inputMessage + "\n");
                writer.newLine();

                //printWriter.println(toWhom + ":" + LocalDateTime.now().format(formatter) + ":" + inputMessage + "\n");
                //printWriter.flush();
                writer.flush();

                inputMessage = inputMessage.replaceAll(" ", "_");
                output.println("to:" + toWhom + ":" + "len:" + inputMessage.length() + ":" + inputMessage);
            }
    }



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public Scanner getScanInput() {
        return scanInput;
    }

    public void setScanInput(Scanner scanInput) {
        this.scanInput = scanInput;
    }






}

