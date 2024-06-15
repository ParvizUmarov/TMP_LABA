package app.server.services;


import app.server.entity.Barber;
import app.server.repository.BarberRepository;

import java.util.Collection;
import java.util.List;

public class BarberCRUDService implements CRUDService<Barber>{

    private final BarberRepository repository;

    public BarberCRUDService(BarberRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Barber> getAll() {
        return repository.getAllBarber();
    }

    @Override
    public void create(Barber object) {

    }

    @Override
    public void update(Barber object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Barber get(Integer id) {
        return null;
    }
}
