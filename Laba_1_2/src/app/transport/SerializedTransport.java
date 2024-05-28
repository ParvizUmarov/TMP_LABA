package app.transport;

import app.IO;
import app.Settings;
import app.transport.message.Message;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SerializedTransport implements Transport {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private final IO logger = new IO();
    private final int port;

    public SerializedTransport() {
        port = Settings.PORT;
    }

    public SerializedTransport(int port) {
        this.port = port;
    }

    public SerializedTransport(Socket socket) {
        port = socket.getPort();
        try {
            this.socket = socket;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            logger.debug(STR."transport joined to socket \{socket}");
        } catch (Exception e) {
            //disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void connect() throws TransportException {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(Settings.HOST, port);
                writer = new ObjectOutputStream(socket.getOutputStream());
                reader = new ObjectInputStream(socket.getInputStream());
                logger.debug(STR."transport connected to \{socket}");
            }
        } catch (Exception e) {
            //disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void disconnect() throws TransportException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.debug(STR."transport disconnected from \{socket}");
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void send(Message m) throws TransportException {
        checkIsConnected();
        try {
            writer.writeObject(m);
            writer.flush();
            logger.debug(STR."transport sended \{m}");
        } catch (Exception e) {
//            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public Message receive() throws TransportException {
        return receive(Message.class);
    }

    @Override
    public <T extends Message> T receive(Class<T> type) throws TransportException {
        logger.debug("in receive()");
        checkIsConnected();
        logger.debug("connection checked");
        try {
            var msg = type.cast(reader.readObject());
            logger.debug(STR."transport received \{msg}");
            return msg;
        } catch (Exception e) {
//            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public InputStream getInputStream() {
        checkIsConnected();
        try {
            return socket.getInputStream();
        } catch (Exception e) {
             throw new TransportException(e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        checkIsConnected();
        try {
            return socket.getOutputStream();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed()) {
            System.out.println(Thread.currentThread().getName() + ": NOT CONNECTED!");
            throw new TransportException("transport closed");
        }
    }

    @Override
    public String toString() {
        return "SerializedTransport{" +
                "socket=" + socket +
                '}';
    }
}
