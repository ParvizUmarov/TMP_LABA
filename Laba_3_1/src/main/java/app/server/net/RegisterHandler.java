package app.server.net;

import app.IO;

import app.server.user.UserService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.RegisterUsernameRequest;

import java.io.File;

public class RegisterHandler extends Handler {
    private final UserService userService;

    public RegisterHandler(Transport transport, IO io, UserService userService) {
        super(transport, io);
        this.userService = userService;
    }

    @Override
    public void handle(Message message) {
        var req = (RegisterUsernameRequest) message;

        var username = req.getUsername();
        io.debug("username - " + username);

//        transport.send(new SuccessResponse());
//
//        var password = transport.receive(RegisterPasswordRequest.class).getPassword();
//        io.debug(STR."password - \{password}");
//        if (!userService.isPasswordValid(password)) {
//            throw new ServerException("password is invalid");
//        }
//
//        File dir = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}");
//
//        dir.mkdir();
//        userService.register(username, password);
//        transport.send(new SuccessResponse());
//
//        io.println(STR."registered \{username}:\{password}");
    }
}
