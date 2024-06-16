package app.transport.message.storage;

import app.server.entity.MessageEntity;
import app.server.entity.Order;
import app.transport.message.Message;

import java.util.Collection;

public class OrdersResponse extends Message {
    private final String userType;
    private final Collection<Order> orders;

    public OrdersResponse(String userType, Collection<Order> orders) {
        this.userType = userType;
        this.orders = orders;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public String getUserType() {
        return userType;
    }
}
