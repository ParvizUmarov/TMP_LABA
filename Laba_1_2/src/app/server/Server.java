package app.server;

import app.IO;
import app.Settings;
import app.server.net.*;
import app.server.session.SessionService;
import app.server.session.Token;
import app.server.user.UserService;
import app.transport.SerializedTransport;
import app.transport.Transport;
import app.transport.message.AuthorizedMessage;
import app.transport.message.ErrorResponse;
import app.transport.message.Message;
import app.transport.message.storage.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final IO io = new IO();
    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();
    private ExecutorService pool = Executors.newCachedThreadPool();
    private List<Transport> transports = new ArrayList<>();
    private List<Transport> clientTransports = Collections.synchronizedList(transports);
    private List<Transport> clientNotificationTransports = Collections.synchronizedList(transports);

    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        new Thread(() -> {
            try (var ss = new ServerSocket(Settings.NOTIFICATION_PORT)) {
                io.println(STR."server notifications listening on port \{Settings.PORT}");
                while (true) {
                    try {
                        var clientSocket = ss.accept();
                        io.println(STR."client connected: \{clientSocket}");
                        clientNotificationTransports.add(new SerializedTransport(
                                clientSocket));
                    } catch (Exception e) {
                        io.println(STR."error: \{e.getMessage()}");
                    }
                }
            } catch (Exception e) {

            } finally {
                pool.shutdown();
            }
        }).start();
        try (var ss = new ServerSocket(Settings.PORT)) {
            io.println(STR."server listening on port \{Settings.PORT}");
            while (true) {
                try {
                    var clientSocket = ss.accept();
                    io.println(STR."client connected: \{clientSocket}");
                    pool.submit(new ClientHandler(clientSocket));
                } catch (Exception e) {
                    io.println(STR."error: \{e.getMessage()}");
                }
            }
        } finally {
            pool.shutdown();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private Transport transport;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                transport = new SerializedTransport(clientSocket);
                clientTransports.add(transport);
                System.out.println(STR."all transports: \{clientTransports}");
//                new Properties().
                while (true) {
                    var request = transport.receive();
                    io.debug("received " + request);
                    checkAuth(request);
                    io.debug("auth checked");
                    routeToHandler(transport, request);
                }
            } catch (Exception e) {
                io.println(STR."handle error: \{e.getMessage()}");
                if (transport != null) {
                    transport.send(new ErrorResponse(e.getMessage()));
                    //transport.disconnect();
                }
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

//    private void handle(Transport transport) {
//        try {
//            while(true){
//                var request = transport.receive();
//                checkAuth(request);
//                routeToHandler(transport, request);
//            }
//        } catch (Exception e) {
//            io.println(STR."handle error: \{e.getMessage()}");
//            transport.send(new ErrorResponse(e.getMessage()));
//        }
//    }

    private void checkAuth(Message request) {
        if (request instanceof AuthorizedMessage auth) {
            if (auth.getAuthToken() == null) {
                throw new ServerException("no authorization token found in request");
            }
            var token = Token.fromText(auth.getAuthToken());
            var session = sessionService.get(token);
            if (session == null) {
                throw new ServerException("authorization failed");
            }
        }
    }

    private void routeToHandler(Transport transport, Message request) {
        (switch (request) {
            case RegisterUsernameRequest req -> new RegisterHandler(transport, io, userService);
            case LoginRequest req -> new LoginHandler(transport, io, userService, sessionService);
            case CheckAuthRequest req -> new CheckAuthHandler(transport, io, sessionService);
            case LogoutRequest req -> new LogoutHandler(transport, io, sessionService);
            case SendMessageRequest req -> new SendMessageHandler(transport, io, sessionService, clientNotificationTransports);
            default -> new UnimplementedHandler(transport, io);
        }).handle(request);
    }
}
