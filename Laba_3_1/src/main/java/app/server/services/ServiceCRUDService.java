package app.server.services;

import app.server.entity.Service;
import app.server.repository.ServiceRepository;

import java.util.Collection;
import java.util.List;

public class ServiceCRUDService implements CRUDService<Service>{

    private ServiceRepository serviceRepository = new ServiceRepository();

    @Override
    public Collection<Service> getAll() {
        return serviceRepository.getServices();
    }

    @Override
    public void create(Service object) {

    }

    @Override
    public void update(Service object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Service get(String name) {
        return null;
    }
}
