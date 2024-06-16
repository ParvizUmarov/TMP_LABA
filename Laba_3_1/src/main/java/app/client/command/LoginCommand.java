package app.client.command;


import app.IO;
import app.client.TokenHolder;
import app.server.entity.Order;
import app.server.user.UserType;
import app.transport.Transport;
import app.transport.message.storage.LoginRequest;
import app.transport.message.storage.LoginResponse;

import java.util.Collection;

public class LoginCommand extends Command {
    private final TokenHolder tokenHolder;

    public LoginCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected(){
        io.println("login as :\na customer -> 1" +
                 "\na barber -> 2");
        io.print("enter number: ");
        int userChoice = Integer.parseInt(io.readln());
        if(userChoice <=2 && userChoice >=1){
            io.print("enter username: ");
            var username = io.readln();
            io.print("enter password: ");
            var password = io.readln();

            transport.send(new LoginRequest(username, password, userChoice));

            var response = expectMessage(LoginResponse.class);
            tokenHolder.setToken(response.getToken());

            io.println("Welcome!");
            userWelcomeText(response.getOrders(), response.getUserType());
            tokenHolder.setUserType(userChoice == 1 ? UserType.CUSTOMER.toString() : UserType.BARBER.toString());
            tokenHolder.setUserMail(response.getUserMail());
        }else{
            io.println( userChoice + ": type of user not found");
        }
    }

    private void userWelcomeText(Collection<Order> orders, String userType){
        if(userType.equals(UserType.BARBER.toString())){
            if(orders.isEmpty()){
                io.println("You don't have any orders!");
            }else{
                io.println("Your orders for today: ");
            }

            for(var order : orders){
               io.println("time: " + order.getTime() +
                          "\n\tcustomer: " + order.getCustomer_id() +
                          "\n\t\tstatus:" + order.getStatus());
               io.println("=================================");
            }
        }


    }

}
