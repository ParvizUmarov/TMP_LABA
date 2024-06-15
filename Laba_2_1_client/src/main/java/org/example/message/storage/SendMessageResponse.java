package org.example.message.storage;

import org.example.entity.MessageEntity;
import org.example.message.Message;

import java.time.LocalDateTime;
import java.util.List;

public class SendMessageResponse extends Message {
    static final long serialVersionUID = -8506502531598632727L;
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
}
