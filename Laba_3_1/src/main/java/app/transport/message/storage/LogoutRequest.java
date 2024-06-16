package app.transport.message.storage;

import app.transport.message.Message;

public class LogoutRequest extends Message {
    private final String mail;
    private final String userType;

    public LogoutRequest(String mail, String userType) {
        this.mail = mail;
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public String getMail() {
        return mail;
    }
}
