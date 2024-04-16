package app.transport.message.storage;

import app.transport.message.Message;

public class LogoutResponse extends Message {
    private final String text;

    public LogoutResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
