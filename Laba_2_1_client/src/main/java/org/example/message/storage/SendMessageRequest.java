package org.example.message.storage;

import org.example.message.Message;

import java.time.LocalDateTime;

public class SendMessageRequest extends Message {
    private final String username;
    private final String message;
    private final LocalDateTime time;

    public SendMessageRequest(String username, String message, LocalDateTime time) {
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
}
