package app.server.handler;

import app.IO;
import app.server.services.BarberCRUDService;
import app.server.services.CustomerCRUDService;
import app.server.services.TokenCRUDService;
import app.server.user.UserType;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.LogoutRequest;

import java.util.Objects;

public class LogoutHandler extends Handler{
    private final TokenCRUDService tokenCRUDService;
    private final BarberCRUDService barberCRUDService;
    private final CustomerCRUDService customerCRUDService;

    public LogoutHandler(Transport transport, IO io, TokenCRUDService tokenCRUDService, BarberCRUDService barberCRUDService, CustomerCRUDService customerCRUDService) {
        super(transport, io);
        this.tokenCRUDService = tokenCRUDService;
        this.barberCRUDService = barberCRUDService;
        this.customerCRUDService = customerCRUDService;

    }

    @Override
    public void handle(Message message) {
        var req = (LogoutRequest) message;
        
        if(Objects.equals(req.getUserType(), UserType.CUSTOMER.toString())){
            customerCRUDService.logout(req.getMail());
        }else{
            barberCRUDService.logout(req.getMail());
        }
        transport.send(new SuccessResponse());
    }
}
