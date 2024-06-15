package app.server.net;

import app.IO;
import app.server.repository.BarberRepository;
import app.server.services.BarberCRUDService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.HelpRequest;
import app.transport.message.storage.HelpResponse;

public class HelpHandler extends Handler{
    public HelpHandler(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    public void handle(Message message) {
        var req = (HelpRequest) message;
        io.println("receive req: " + req.getClass());
        BarberRepository barberRepository = new BarberRepository();
        BarberCRUDService service = new BarberCRUDService(barberRepository);

//        transport.send(new HelpResponse("All commands: " +
//                "\n-> exit" +
//                "\n-> register" +
//                "\n-> login" +
//                "\n-> token" +
//                "\n-> check auth" +
//                "\n-> help\n"));

        transport.send(new HelpResponse(service.getAll().toString()));

    }
}
