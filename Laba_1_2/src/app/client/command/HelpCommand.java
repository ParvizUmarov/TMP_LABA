package app.client.command;

import app.IO;
import app.transport.Transport;

public class HelpCommand extends Command{
    public HelpCommand(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    public void perform() {
        super.perform();
        io.print("All commands: " +
                "\n-> exit" +
                "\n-> register" +
                "\n-> login" +
                "\n-> token" +
                "\n-> check auth" +
                "\n-> help\n");
    }
}
