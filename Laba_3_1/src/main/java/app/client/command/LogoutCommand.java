package app.client.command;

import app.IO;
import app.client.TokenHolder;
import app.transport.Transport;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.LogoutRequest;

public class LogoutCommand extends Command{
    private final TokenHolder tokenHolder;
    public LogoutCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws Exception {
        transport.send(new LogoutRequest(tokenHolder.getUserMail(), tokenHolder.getUserType()));
        expectMessage(SuccessResponse.class);
        io.println("Bye!");
    }
}
