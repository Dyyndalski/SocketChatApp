package client;

import java.time.LocalDateTime;

public class Holder{

    private String person;
    private LocalDateTime dateTime;
    private String message;
    private String type;

    public Holder(String person, LocalDateTime dateTime, String message, String type) {
        this.person = person;
        this.dateTime = dateTime;
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }
    public String getPerson() {
        return person;
    }
    public String getType() {
        return this.type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "type=" + type + '\'' +
                "person='" + person + '\'' +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                '}' + "\n";
    }
}
