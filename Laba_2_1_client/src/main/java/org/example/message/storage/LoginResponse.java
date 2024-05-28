package org.example.message.storage;

import org.example.message.Message;

public class LoginResponse extends Message {
    private final boolean isLoginSuccess;

    public LoginResponse(boolean isLoginSuccess) {
        this.isLoginSuccess = isLoginSuccess;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }
}