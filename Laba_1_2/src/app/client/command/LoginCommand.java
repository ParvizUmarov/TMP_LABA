package app.client.command;

import app.IO;
import app.Settings;
import app.client.MessageListener;
import app.client.TokenHolder;
import app.transport.SerializedTransport;
import app.transport.Transport;
import app.transport.message.storage.LoginRequest;
import app.transport.message.storage.LoginResponse;
import app.ui.UI;

public class LoginCommand extends Command {
    private final TokenHolder tokenHolder;

    public LoginCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() {
        io.print("enter username: ");
        var username = io.readln();
        io.print("enter password: ");
        var password = io.readln();
        transport.send(new LoginRequest(username, password));

        var response = expectMessage(LoginResponse.class);
        io.print(STR."\{UI.CHAT_WELCOME_TITLE}");
        tokenHolder.setToken(response.getToken());

        io.println(STR."User successfully login with token \{tokenHolder.getToken()}");

        var thread = new Thread(new MessageListener(new SerializedTransport(
                Settings.NOTIFICATION_PORT), io));
        thread.setName("MessageListenerThread");
        thread.start();
    }

//    public static void main(String[] args) {
//        var list = new ArrayList<>(List.of(1,2,3));
//        var iter = list.listIterator();
//        while (iter.hasNext()) {
//            var i = iter.next();
//            System.out.println("i = " + i);
//            if (i == 2) {
//                iter.add(4);
//            }
//        }
//        System.out.println(list);
//    }
}
