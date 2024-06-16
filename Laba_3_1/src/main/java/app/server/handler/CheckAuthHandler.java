package app.server.handler;

import app.IO;
import app.server.ServerException;
import app.server.services.BarberCRUDService;
import app.server.services.CustomerCRUDService;
import app.server.services.TokenCRUDService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.CheckAuthRequest;
import app.transport.message.storage.CheckAuthResponse;

public class CheckAuthHandler extends Handler {
    private final SessionService sessionService;
    private final TokenCRUDService tokenCRUDService;

    public CheckAuthHandler(Transport transport, IO io, SessionService sessionService, TokenCRUDService tokenCRUDService) {
        super(transport, io);
        this.sessionService = sessionService;
        this.tokenCRUDService = tokenCRUDService;
    }

    @Override
    public void handle(Message message) {
        var req = (CheckAuthRequest) message;

        var session = sessionService.get(Token.fromText(req.getAuthToken()));
        var mail = session.getString(Session.MAIL);
        var userType = session.getUserType();

        switch (userType){
            case BARBER :
                    var barberToken = tokenCRUDService.get(mail);
                    if(barberToken != null){
                        var response = "BARBER PROFILE: \n" +
                                "mail: " + mail +
                                "\n\ttoken: " + barberToken.getToken();
                        transport.send(new CheckAuthResponse(response));
                    }else{
                        throw new ServerException("user doesn't authorized");
                    }
            case CUSTOMER :
                var customerToken = tokenCRUDService.get(mail);
                if(customerToken != null){
                    var response = "CUSTOMER PROFILE: \n" +
                            "mail: " + mail +
                            "\n\ttoken: " + customerToken.getToken();
                    transport.send(new CheckAuthResponse(response));
                }else{
                    throw new ServerException("user doesn't authorized");
                }
        }

    }
}
