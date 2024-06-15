package app.server.services;

import app.server.entity.Customer;

import java.util.Collection;
import java.util.List;

public class CustomerCRUDService implements CRUDService<Customer> {


    @Override
    public Collection<Customer> getAll() {
        return List.of();
    }

    @Override
    public void create(Customer object) {

    }

    @Override
    public void update(Customer object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Customer get(Integer id) {
        return null;
    }
}
