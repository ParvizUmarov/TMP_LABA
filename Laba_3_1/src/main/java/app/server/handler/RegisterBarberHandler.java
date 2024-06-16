package app.server.handler;

import app.IO;

import app.server.entity.Barber;
import app.server.services.BarberCRUDService;
import app.server.services.SalonCRUDService;
import app.server.services.ServiceCRUDService;
import app.server.user.UserService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.*;

public class RegisterBarberHandler extends Handler {
    private final BarberCRUDService barberCRUDService;
    private final SalonCRUDService salonCRUDService;
    private final ServiceCRUDService serviceCRUDService;

    public RegisterBarberHandler(Transport transport, IO io, BarberCRUDService barberCRUDService, SalonCRUDService salonCRUDService, ServiceCRUDService serviceCRUDService) {
        super(transport, io);
        this.barberCRUDService = barberCRUDService;
        this.salonCRUDService = salonCRUDService;
        this.serviceCRUDService = serviceCRUDService;
    }

    @Override
    public void handle(Message message) {
        var req = (RegisterBarberRequest) message;

        var username = req.getUsername();
        var surname = req.getSurname();
        var birthday = req.getBirthday();
        var phone = req.getPhone();
        var mail = req.getMail();
        var password = req.getPassword();
        var authState = false;
        var workExperience = req.getWorkExperience();

        io.println("send register salon id: " + salonCRUDService.getAll());
        transport.send(new RegisterSalonIdResponse(salonCRUDService.getAll()));
        var salonIDMsg =  transport.receive(RegisterSalonIdRequest.class);

        var salonID = salonIDMsg.getSalonId();

        transport.send(new RegisterServiceIdResponse(serviceCRUDService.getAll()));
        var serviceIdMsg = transport.receive(RegisterServiceIdRequest.class);

        var serviceID = serviceIdMsg.getServiceID();

        barberCRUDService.create(new Barber(0,
                username, surname, birthday, phone, mail, password, authState, workExperience, salonID, serviceID));

        transport.send(new SuccessResponse());

    }
}
