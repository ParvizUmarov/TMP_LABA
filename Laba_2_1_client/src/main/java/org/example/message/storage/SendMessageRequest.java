package org.example.message.storage;

import org.example.message.Message;

import java.time.LocalDateTime;

public class SendMessageRequest extends Message {
    private final String username;
    private final String message;
    private final LocalDateTime time;
    static final long serialVersionUID = 1364992340983372449L;
    private final boolean isFirstTime;

    public SendMessageRequest(String username, String message, LocalDateTime time, boolean isFirstTime) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.isFirstTime = isFirstTime;
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
