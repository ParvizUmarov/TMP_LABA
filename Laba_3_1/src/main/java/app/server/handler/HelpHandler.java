package app.server.handler;

import app.IO;
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

        transport.send(new HelpResponse("All commands: " +
                "\n-> exit" +
                "\n-> register" +
                "\n-> login" +
                "\n-> token" +
                "\n-> check auth" +
                "\n-> help\n"));
    }
}
