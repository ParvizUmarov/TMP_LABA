package org.example.transport;

import org.example.IO;
import org.example.Settings;
import org.example.message.Message;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Transport {
    private Socket socket;
    private DataOutputStream writer;
    private DataInputStream reader;
    private final IO logger = new IO();

    public Transport(){}

    public void connect(){
        try{
            socket = new Socket(Settings.HOST, Settings.PORT);
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(socket.getInputStream());
            logger.debug("transport connected to " + socket);

        }catch (Exception e){
            throw new TransportException(e);
        }
    }

    public void disconnect() throws TransportException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.println("transport disconnected from " + socket);
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    public void send(Message m) throws TransportException {
        checkIsConnected();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(m);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + byteArray.length);
            byteBuffer.putInt(byteArray.length);
            byteBuffer.put(byteArray);
            System.out.println("byte array len: " + byteBuffer.array().length);

            writer.write(byteBuffer.array());
            writer.flush();

            logger.debug("transport sended: " + m);
        } catch (Exception e) {
//            disconnect();
            throw new TransportException(e);
        }
    }

    public Message receiveMessage() throws TransportException {
        checkIsConnected();
        try {
            int length = reader.readInt();
            System.out.println("length: " + length);
            if (length <= 0) {
                throw new TransportException("Invalid length received: " + length);
            }

            byte[] data = new byte[length];
            reader.readFully(data);

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Message message = (Message) ois.readObject();
            return message;
        } catch (StreamCorruptedException e) {
            System.err.println("StreamCorruptedException: " + e.getMessage());
            throw new TransportException(e);
        } catch (Exception e) {
            System.err.println("Error receiving message: " + e.getMessage());
            throw new TransportException(e);
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed()) {
            System.out.println(Thread.currentThread().getName() + ": NOT CONNECTED!");
            throw new TransportException("transport closed");
        }
    }
}