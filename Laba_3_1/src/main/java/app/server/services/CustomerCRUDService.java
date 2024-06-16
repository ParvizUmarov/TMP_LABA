package app.server.services;

import app.server.entity.Customer;
import app.server.repository.CustomerRepository;

import java.util.Collection;
import java.util.List;

public class CustomerCRUDService implements CRUDService<Customer> {


    private CustomerRepository repository = new CustomerRepository();

    @Override
    public Collection<Customer> getAll() {
        return List.of();
    }

    @Override
    public void create(Customer customer) {
       repository.registerCustomer(customer);
    }

    @Override
    public void update(Customer object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Customer get(String name) {
        return repository.getCustomerByName(name);
    }

    public Customer checkPassword(String username, String password){
        var customer = repository.getCustomerByName(username);
        if(customer != null){
            if(customer.password().equals(password)){
                repository.changeAuthState(customer.mail(), true);
                return customer;
            }else {
                return null;
            }
        }else{
            return null;
        }
    }

    public void logout(String mail){
        repository.changeAuthState(mail, false);
    }
}
