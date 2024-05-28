package app.server.net;

import app.IO;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.LogoutRequest;
import app.transport.message.storage.LogoutResponse;

public class LogoutHandler extends Handler{

    private final SessionService sessionService;
    public LogoutHandler(Transport transport, IO io, SessionService sessionService) {
        super(transport, io);
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (LogoutRequest) message;
        Token token = Token.fromText(req.getToken());
        var username = sessionService.get(token).getString(Session.USERNAME);
        sessionService.remove(token);
        transport.send(new LogoutResponse(STR."user \{username} logout in with token \{token}"));
        io.println(STR."user \{username} logout in with token \{token}");

    }
}
