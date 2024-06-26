package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.ErrorResponse;
import app.transport.message.Message;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class Command {
    protected final Transport transport;
    protected final IO io;
    private AtomicInteger counter = new AtomicInteger(20);

    public Command(Transport transport, IO io) {
        this.transport = transport;
        this.io = io;
    }

    public void perform() {
        try {
            transport.connect();
            if(counter.get() == 0){
                transport.disconnect();
            }
            counter.addAndGet(10);
            performConnected();
        } catch (Exception e) {
            throw new CommandException(e);
        } finally {
            transport.disconnect();
        }
    }

    protected void performConnected() throws Exception {
        /* do nothing */
    }

    public <T extends Message> T expectMessage(Class<T> type) {
        var msg = transport.receive();
        if (msg instanceof ErrorResponse error) {
            throw new CommandException(error.getMessage());
        }
        try {
            return type.cast(msg);
        } catch (ClassCastException e) {
            throw new CommandException("unexpected received message type - " + msg.getClass().getSimpleName());
        }
    }

    {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    if(counter.get() > -1){
                        counter.decrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

}
