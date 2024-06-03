package org.example.transport;

import org.example.IO;
import org.example.Settings;
import org.example.message.Message;
import org.example.message.ResponseListener;
import org.example.message.storage.LoginResponse;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

//    public String receiveSignal() throws TransportException {
//        checkIsConnected();
//        try {
//            byte[] signalBuffer = new byte[5];
//            reader.readFully(signalBuffer);
//            return new String(signalBuffer, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new TransportException("Error receiving signal: " + e.getMessage());
//        }
//    }

    public Message receiveMessage() throws TransportException {
        checkIsConnected();
        try {

            System.out.println("available " + reader.available());
            byte[] lengthBuffer = new byte[4];
            reader.readFully(lengthBuffer); // Чтение 4 байт, представляющих длину сообщения

            int length = ByteBuffer.wrap(lengthBuffer).getInt(); // Преобразование 4 байт в int
            if (length <= 0) {
                throw new TransportException("Invalid length received: " + length);
            }

            byte[] byteArray = new byte[1024];
            reader.readFully(byteArray); // Чтение полезной нагрузки

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Message) objectInputStream.readObject(); // Десериализация полезной нагрузки в объект
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
}