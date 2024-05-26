package app.client;

import app.IO;
import app.client.command.*;
import app.transport.SerializedTransport;
import app.transport.Transport;
import app.transport.TransportException;
import app.ui.UI;

public class Client {
    private final IO io = new IO();
    private static final Transport transport = new SerializedTransport();
    private final TokenHolder tokenHolder = new TokenHolder();
    private boolean connected = false;

    public static void main(String[] args) {
        new Client().commandLoop();
    }

    private void commandLoop() {
        String userInput;
        do {
            io.print(">>> ");
            userInput = io.readln().strip().toLowerCase();
            final String unrecognizedCommand = STR."\{userInput}: Command not found. Try <help>";
            try {
                (switch (userInput) {
                    case "exit" -> new PrintCommand(transport, io, "bye!");
                    case "register" -> new RegisterCommand(transport, io);
                    case "login" -> new LoginCommand(transport, io, tokenHolder);
                    case "token" -> new PrintCommand(transport, io, tokenHolder.getToken());
                    case "check auth" -> new CheckAuthCommand(transport, io, tokenHolder);
                    case "logout" -> new LogoutCommand(transport, io, tokenHolder.getToken());
                    case "help" -> new HelpCommand(transport, io);
                    case "send" -> new SendMessageCommand(transport, io, tokenHolder);
                    default -> new PrintCommand(transport, io, unrecognizedCommand);
                }).perform();
            } catch (Exception e) {
                io.println(STR."Error: \{e.getMessage()}");
            }
        } while (!"exit".equals(userInput));
    }
}
