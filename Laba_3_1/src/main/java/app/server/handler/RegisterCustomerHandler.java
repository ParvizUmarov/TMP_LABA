package app.server.handler;

import app.IO;
import app.server.entity.Customer;
import app.server.services.CustomerCRUDService;
import app.server.user.UserService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.RegisterCustomerRequest;

public class RegisterCustomerHandler extends Handler {
    private final CustomerCRUDService service;

    public RegisterCustomerHandler(Transport transport, IO io, CustomerCRUDService service) {
        super(transport, io);
        this.service = service;
    }

    @Override
    public void handle(Message message) {
        var req = (RegisterCustomerRequest) message;

        var username = req.getUsername();
        var surname = req.getSurname();
        var birthday = req.getBirthday();
        var phone = req.getPhone();
        var mail = req.getMail();
        var password = req.getPassword();
        var authState = false;

        service.create(new Customer(null, username, surname, birthday, phone, mail, password, authState));

        transport.send(new SuccessResponse());
        io.println("registered " + username);
    }
}
