package app.server.handler;

import app.IO;
import app.server.ServerException;
import app.server.services.BarberCRUDService;
import app.server.services.CustomerCRUDService;
import app.server.services.OrdersCRUDService;
import app.server.services.TokenCRUDService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.user.UserType;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.LoginRequest;
import app.transport.message.storage.LoginResponse;
import app.transport.message.storage.OrdersResponse;

public class LoginHandler extends Handler {
    private final SessionService sessionService;
    private final static long TIME_OUT = 20000;
    private final BarberCRUDService barberCRUDService;
    private final CustomerCRUDService customerCRUDService;
    private final OrdersCRUDService ordersCRUDService;
    private final TokenCRUDService tokenCRUDService;


    public LoginHandler(Transport transport, IO io, SessionService sessionService, BarberCRUDService barberCRUDService, CustomerCRUDService customerCRUDService, OrdersCRUDService ordersCRUDService, TokenCRUDService tokenCRUDService) {
        super(transport, io);
        this.sessionService = sessionService;
        this.barberCRUDService = barberCRUDService;
        this.customerCRUDService = customerCRUDService;
        this.ordersCRUDService = ordersCRUDService;
        this.tokenCRUDService = tokenCRUDService;
    }

    @Override
    public void handle(Message message) {
        var req = (LoginRequest) message;
        var userType = req.getUserType();
        if(userType == 1){
            var checkPassword = customerCRUDService.checkPassword(req.getUsername(), req.getPassword());
            if (checkPassword == null) {
                throw new ServerException("username or password are incorrect");
            }
            var pair = sessionService.create(tokenCRUDService.get(checkPassword.mail()).getToken());
            var token = pair.token();
            var session = pair.session();
            session.put(Session.MAIL, checkPassword.mail());
            session.setUserType(UserType.CUSTOMER);
            transport.send(new LoginResponse(token.getText(), UserType.CUSTOMER.toString(), ordersCRUDService.getCustomerOrders(checkPassword.id(), UserType.CUSTOMER), checkPassword.mail()));
            io.println("user "+req.getUsername()+" logged in with token " + token);
        }else{
            var checkPassword = barberCRUDService.checkPassword(req.getUsername(), req.getPassword());
            if (checkPassword == null) {
                throw new ServerException("username or password are incorrect");
            }
            var pair = sessionService.create(tokenCRUDService.get(checkPassword.mail()).getToken());
            var token = pair.token();
            var session = pair.session();
            session.put(Session.MAIL, checkPassword.mail());
            session.setUserType(UserType.BARBER);
            transport.send(new LoginResponse(token.getText(), UserType.BARBER.toString(), ordersCRUDService.getCustomerOrders(checkPassword.id(), UserType.BARBER), checkPassword.mail() ));
            io.println("user "+req.getUsername()+" logged in with token " + token);

        }

//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                sessionService.remove(token);
//                io.print("\nТайм аут простоя сетевого соединение");
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(tt, TIME_OUT);
    }
}
