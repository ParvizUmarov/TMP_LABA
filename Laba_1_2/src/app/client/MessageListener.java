package app.client;

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
        transport.connect();
        // TODO send token

        while (true) {
            try {
                transport.connect();
                var message = transport.receive();
                io.debug(STR."MessageListener received \{message}");
                if (message instanceof SendMessageResponse sendMessageResponse) {
                    var messageUser = STR."user: \{sendMessageResponse.getSender()}";
                    var messageTime = STR."time: \{sendMessageResponse.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))}";
                    var messageText = STR."message: \{sendMessageResponse.getMessage()}";
                    var response = STR."\{messageUser}\n\{messageText}\n\{messageTime}";
                    io.println("--------------------------");
                    io.println(response);
                    io.println("--------------------------");
                }
            } catch (Exception e) {
                io.println("Error in MessageListener: " + e.getMessage());
            } finally {
                io.debug("MessageListener finished");
            }
        }
    }
}
