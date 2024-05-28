package app.transport.message.storage;

import app.transport.message.Message;

public class LogoutRequest extends Message {
    private final String token;

    public LogoutRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
