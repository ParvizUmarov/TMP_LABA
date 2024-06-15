package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.storage.HelpRequest;
import app.transport.message.storage.HelpResponse;

public class HelpCommand extends Command{
    public HelpCommand(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    protected void performConnected() throws Exception {
        transport.send(new HelpRequest());

        var response = expectMessage(HelpResponse.class);
        io.println(response.getHelpCommand());
    }
}
