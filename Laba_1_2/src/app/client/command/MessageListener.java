package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.storage.SendMessageResponse;

import java.time.format.DateTimeFormatter;

public class MessageListener implements Runnable {
    private final Transport transport;
    private final IO io;

    public MessageListener(Transport transport, IO io) {
        this.transport = transport;
        this.io = io;
    }

    @Override
    public void run() {
        try {
            while (true) {
                var message = transport.receive();
                if (message instanceof SendMessageResponse sendMessageResponse) {
                    var messageUser = STR."user: \{sendMessageResponse.getSender()}";
                    var messageTime = STR."time: \{sendMessageResponse.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))}";
                    var messageText = STR."message: \{sendMessageResponse.getMessage()}";
                    var response = STR."\{messageUser}\n\{messageText}\n\{messageTime}";
                    io.println("--------------------------");
                    io.println(response);
                    io.println("--------------------------");
                }
            }
        } catch (Exception e) {
            io.println("Error in MessageListener: " + e.getMessage());
        }
    }
}
