package app.transport.message.storage;

import app.server.entity.Salon;
import app.transport.message.Message;

import java.util.Collection;

public class RegisterSalonIdResponse extends Message {
    private final Collection<Salon> salons;

    public RegisterSalonIdResponse(Collection<Salon> salons) {
        this.salons = salons;
    }

    public Collection<Salon> getSalons() {
        return salons;
    }

}
