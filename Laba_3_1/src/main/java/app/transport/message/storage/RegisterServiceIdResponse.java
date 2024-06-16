package app.transport.message.storage;

import app.server.entity.Service;
import app.transport.message.Message;

import java.util.Collection;

public class RegisterServiceIdResponse extends Message {
    private final Collection<Service> services;

    public RegisterServiceIdResponse(Collection<Service> services) {
        this.services = services;
    }

    public Collection<Service> getServices() {
        return services;
    }
}
