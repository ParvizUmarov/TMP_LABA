package app.server.services;

import java.util.Collection;

public interface CRUDService<T> {
    Collection<T> getAll();
    void create(T object);
    void update(T object);
    void delete(Integer id);
    T get(String name);
}
