package app.transport.message.storage;

import app.server.entity.Order;
import app.server.user.UserType;
import app.transport.message.Message;

import java.util.Collection;

public class LoginResponse extends Message {
    private final String token;
    private final String userType;
    private final Collection<Order> orders;
    private final String userMail;

    public LoginResponse(String token, String userType, Collection<Order> orders, String userMail) {
        this.token = token;
        this.userType = userType;
        this.orders = orders;
        this.userMail = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public String getUserType() {
        return userType;
    }

    public String getToken() {
        return token;
    }
}
