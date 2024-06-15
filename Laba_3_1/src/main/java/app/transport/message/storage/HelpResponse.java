package app.transport.message.storage;

import app.transport.message.Message;

public class HelpResponse extends Message {
    private final String helpCommand;

    public HelpResponse(String helpCommand) {
        this.helpCommand = helpCommand;
    }

    public String getHelpCommand() {
        return helpCommand;
    }
}
