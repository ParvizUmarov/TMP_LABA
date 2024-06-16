package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.*;

public class RegisterCommand extends Command{
    public RegisterCommand(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    protected void performConnected() throws Exception {
        io.println("register as :\na customer -> 1" +
                "\na barber -> 2");
        io.print("enter number: ");

        int userChoice = Integer.parseInt(io.readln());

        if(userChoice == 1){
            io.print("enter username: ");
            var username = io.readln();

            io.print("enter surname: ");
            var surname = io.readln();

            io.print("enter birthday: ");
            var birthday = io.readln();

            io.print("enter phone: ");
            var phone = io.readln();

            io.print("enter mail: ");
            var mail = io.readln();

            io.print("enter password: ");
            var password = io.readln();

            transport.send(new RegisterCustomerRequest(username, surname, birthday, phone, mail, password));
            expectMessage(SuccessResponse.class);

            io.println("Customer successfully registered");
        }else if(userChoice == 2){
            io.print("enter username: ");
            var username = io.readln();

            io.print("enter password: ");
            var password = io.readln();

            io.print("enter surname: ");
            var surname = io.readln();

            io.print("enter birthday: ");
            var birthday = io.readln();

            io.print("enter phone: ");
            var phone = io.readln();

            io.print("enter mail: ");
            var mail = io.readln();

            io.print("enter workExperience: ");
            var workExperience = Integer.parseInt(io.readln());

            transport.send(new RegisterBarberRequest(
                    username, surname, birthday, phone, mail, password, workExperience));
            var salonIdResponse = expectMessage(RegisterSalonIdResponse.class);

            io.println("Recommendation:");
            io.println("| id |   address   ");
            for(var salon : salonIdResponse.getSalons()){
                io.println("| " + salon.getId() + " | " + salon.getAddress());
            }

            io.print("enter salonID: ");
            var salonID = Integer.parseInt(io.readln());

            transport.send(new RegisterSalonIdRequest(salonID));
            var serviceIdResponse = expectMessage(RegisterServiceIdResponse.class);

            io.println("Services:" );
            io.println("| id |        name        |  price  ");
            for(var service : serviceIdResponse.getServices()){
                io.println("| " + service.getId() + " | " + service.getName() + " | " + service.getPrice());
            }

            io.print("enter servicesID: ");
            var servicesID = Integer.parseInt(io.readln());
            transport.send(new RegisterServiceIdRequest(servicesID));

            expectMessage(SuccessResponse.class);
            io.println("Barber successfully registered");
        }else{
            io.println( userChoice + ": type of user not found");
        }
    }
}
