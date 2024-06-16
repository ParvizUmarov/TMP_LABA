package app.server.services;

import app.server.entity.MessageEntity;

import java.util.Collection;
import java.util.List;

public class MessageCRUDService implements CRUDService<MessageEntity> {


    @Override
    public Collection<MessageEntity> getAll() {
        return List.of();
    }

    @Override
    public void create(MessageEntity object) {

    }

    @Override
    public void update(MessageEntity object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public MessageEntity get(String id) {
        return null;
    }
}
