package app.server.services;

import app.server.entity.Order;
import app.server.repository.OrdersRepository;
import app.server.user.UserType;

import java.util.Collection;
import java.util.List;

public class OrdersCRUDService implements CRUDService<Order> {

    private OrdersRepository repository = new OrdersRepository();

    @Override
    public Collection<Order> getAll() {
        return List.of();
    }

    @Override
    public void create(Order object) {

    }

    @Override
    public void update(Order object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Order get(String id) {
        return null;
    }

    public Collection<Order> getCustomerOrders(int id, UserType userType){
        return repository.getLastOrder(id, userType);
    }

}
