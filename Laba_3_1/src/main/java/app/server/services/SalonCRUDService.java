package app.server.services;

import app.server.entity.Salon;
import app.server.repository.SalonRepository;

import java.util.Collection;
import java.util.List;

public class SalonCRUDService implements CRUDService<Salon> {

    private SalonRepository salonRepository = new SalonRepository();

    @Override
    public Collection<Salon> getAll() {
        return salonRepository.getSalons();
    }

    @Override
    public void create(Salon object) {

    }

    @Override
    public void update(Salon object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Salon get(String name) {
        return null;
    }
}
