package app.server.services;


import app.server.entity.Chat;

import java.util.Collection;
import java.util.List;

public class ChatCRUDService implements CRUDService<Chat>{


    @Override
    public Collection<Chat> getAll() {
        return List.of();
    }

    @Override
    public void create(Chat object) {

    }

    @Override
    public void update(Chat object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Chat get(String id) {
        return null;
    }
}
