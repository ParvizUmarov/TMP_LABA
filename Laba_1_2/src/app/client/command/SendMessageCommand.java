package app.client.command;

import app.IO;
import app.client.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.SendMessageRequest;
import app.transport.message.storage.SendMessageResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendMessageCommand extends Command{
    private final TokenHolder tokenHolder;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public SendMessageCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() throws Exception {
        io.print("write a message: ");
        var input = io.readln();
        var time = LocalDateTime.now();

        transport.send(new SendMessageRequest(tokenHolder.getToken(),input,time));

//        var messageListener = new MessageListener(transport, io);
//        Executors.newSingleThreadExecutor().submit(messageListener);

//        var expectedMessage = expectMessage(SendMessageResponse.class);
//
//        var messageUser = STR."user: \{expectedMessage.getSender()}";
//        var messageTime = STR."time: \{expectedMessage.getTime().format(formatter)}";
//        var message = STR."message: \{expectedMessage.getMessage()}";
//
//        var response = STR."\{messageUser}\n\{message}\n\{messageTime}";
//        io.println("--------------------------");
//        io.println(response);
//        io.println("--------------------------");

    }

    {
        var t = new Thread(() -> {
            while (true) {
                try {
                    //var message = transport.receive();
                    var expectedMessage = expectMessage(SendMessageResponse.class);
                    if(expectedMessage != null){
                        var messageUser = STR."user: \{expectedMessage.getSender()}";
                        var messageTime = STR."time: \{expectedMessage.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))}";
                        var messageText = STR."message: \{expectedMessage.getMessage()}";
                        var response = STR."\{messageUser}\n\{messageText}\n\{messageTime}";
                        io.println("--------------------------");
                        io.println(response);
                        io.println("--------------------------");
                    }
//                    if (message instanceof SendMessageResponse sendMessageResponse) {
//                        var messageUser = STR."user: \{sendMessageResponse.getSender()}";
//                        var messageTime = STR."time: \{sendMessageResponse.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))}";
//                        var messageText = STR."message: \{sendMessageResponse.getMessage()}";
//                        var response = STR."\{messageUser}\n\{messageText}\n\{messageTime}";
//                        io.println("--------------------------");
//                        io.println(response);
//                        io.println("--------------------------");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

}

