package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.storage.LogoutRequest;
import app.transport.message.storage.LogoutResponse;

public class LogoutCommand extends Command{

    String token;

    public LogoutCommand(Transport transport, IO io, String token) {
        super(transport, io);
        this.token = token;
    }

    @Override
    protected void performConnected() throws Exception {
        transport.send(new LogoutRequest(token));
        var response = expectMessage(LogoutResponse.class);
        io.println(response.getText());

    }
}
