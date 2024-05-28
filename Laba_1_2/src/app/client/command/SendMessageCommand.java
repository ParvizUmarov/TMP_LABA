package app.client.command;

import app.IO;
import app.client.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.SendMessageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendMessageCommand extends Command{
    private final TokenHolder tokenHolder;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public SendMessageCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() throws Exception {
        io.print("write a message: ");
        var input = io.readln();
        var time = LocalDateTime.now();

        transport.send(new SendMessageRequest(tokenHolder.getToken(),input,time));

    }

}

