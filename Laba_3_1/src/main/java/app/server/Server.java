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
import app.transport.message.storage.HelpRequest;
import app.transport.message.storage.LoginRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final IO io = new IO();
    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();
    private ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        try (var ss = new ServerSocket(Settings.PORT)) {
            io.println("server listening on port " + Settings.PORT);
            while (true) {
                try {
                    var clientSocket = ss.accept();
                    io.println("client connected: " + clientSocket);
                    pool.submit(() -> handle(new SerializedTransport(clientSocket)));
                } catch (Exception e) {
                    io.println("error: " + e.getMessage());
                }
            }
        } finally {
            pool.shutdown();
        }
    }

    private void handle(Transport transport) {
        try {
            var request = transport.receive();
            checkAuth(request);
            if (request instanceof AuthorizedMessage auth) {
                var token = Token.fromText(auth.getAuthToken());
                System.out.println("has request");
                sessionService.hasRequest(token);
            }
            routeToHandler(transport, request);

        } catch (Exception e) {
            io.println("handle error: " + e.getMessage());
            transport.send(new ErrorResponse(e.getMessage()));
        } finally {
            transport.disconnect();
        }
    }

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
            case HelpRequest req -> new HelpHandler(transport, io);
            case LoginRequest req -> new LoginHandler(transport, io, userService, sessionService);
            default -> new UnimplementedHandler(transport, io);
        }).handle(request);
    }
}
