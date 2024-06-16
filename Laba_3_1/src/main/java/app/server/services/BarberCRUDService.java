package app.server.services;


import app.server.entity.Barber;
import app.server.repository.BarberRepository;

import java.util.Collection;

public class BarberCRUDService implements CRUDService<Barber>{

    private final BarberRepository repository= new BarberRepository();

    @Override
    public Collection<Barber> getAll() {
        return repository.getAllBarber();
    }

    @Override
    public void create(Barber barber) {
       repository.registerBarber(barber);
    }

    @Override
    public void update(Barber object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Barber get(String name) {
        return repository.getBarberByName(name);
    }

    public Barber checkPassword(String username, String password){
        var barber = repository.getBarberByName(username);
        if(barber != null){
            if(barber.password().equals(password)){
                repository.changeAuthState(barber.mail(), true);
                return barber;
            }else{
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
