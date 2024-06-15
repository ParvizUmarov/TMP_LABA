package org.example.message.storage;

import org.example.entity.MessageEntity;
import org.example.message.Message;

import java.util.List;

public class LoginResponse extends Message {
    static final long serialVersionUID = -3104430110442135533L;
    private final boolean isLoginSuccess;

    public LoginResponse(boolean isLoginSuccess) {
        this.isLoginSuccess = isLoginSuccess;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }
}