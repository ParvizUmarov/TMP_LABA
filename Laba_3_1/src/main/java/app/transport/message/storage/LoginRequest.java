package app.transport.message.storage;

import app.transport.message.Message;

public class LoginRequest extends Message {
    private final String username;
    private final String password;
    private final int userType;

    public LoginRequest(String username, String password, int userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getUserType() {
        return userType;
    }
}
