package org.example.message.storage;

import org.example.message.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SendMessageResponse extends Message {
    private final String username;
    private final String message;
    private final LocalDateTime time;

    public SendMessageResponse(String username, String message, LocalDateTime time) {
        this.username = username;
        this.message = message;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "SendMessageResponse{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
