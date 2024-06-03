package org.example.message.storage;

import org.example.message.Message;

import java.time.LocalDateTime;

public class SendMessageResponse extends Message {
    private final String token;
    private final String message;
    private final LocalDateTime time;

    public SendMessageResponse(String token, String message, LocalDateTime time) {
        this.token = token;
        this.message = message;
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
