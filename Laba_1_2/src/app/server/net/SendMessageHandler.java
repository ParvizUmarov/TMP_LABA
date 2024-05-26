package app.server.net;

import app.IO;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.SendMessageRequest;
import app.transport.message.storage.SendMessageResponse;

import java.util.List;

public class SendMessageHandler extends Handler{
    private final SessionService sessionService;
    private final List<Transport> transports;
    public SendMessageHandler(Transport transport, IO io, SessionService sessionService, List<Transport> transports) {
        super(transport, io);
        this.sessionService = sessionService;
        this.transports = transports;
    }

    @Override
    public void handle(Message message) {
        var req = (SendMessageRequest) message;
        Token token = Token.fromText(req.getToken());
        var username = sessionService.get(token).getString(Session.USERNAME);

        transport.send(new SendMessageResponse(username, req.getMessage(), req.getTime()));
        for(var newTransport : transports){
            if(newTransport != transport){
                newTransport.send(new SendMessageResponse(username, req.getMessage(), req.getTime()));
                io.println(STR."send message to \{newTransport}");
            }
        }
        io.println(STR."user <\{username}> send message <<\{req.getMessage()}>> time \{req.getTime()}");
    }
}
