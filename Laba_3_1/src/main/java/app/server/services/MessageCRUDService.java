package app.server.services;

import app.server.entity.Message;

import java.util.Collection;
import java.util.List;

public class MessageCRUDService implements CRUDService<Message> {


    @Override
    public Collection<Message> getAll() {
        return List.of();
    }

    @Override
    public void create(Message object) {

    }

    @Override
    public void update(Message object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Message get(Integer id) {
        return null;
    }
}
