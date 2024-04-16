package app.server.net;

import app.IO;
import app.Settings;
import app.client.command.CommandException;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.DeleteFileRequest;
import app.transport.message.storage.DeleteFileResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileHandler extends Handler{

    private final FileStorageService fileSystemService;
    private final SessionService sessionService;
    public DeleteFileHandler(Transport transport, IO io, FileStorageService fileSystemService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileSystemService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (DeleteFileRequest) message;

        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);
        var filename = req.getFilename();
        var filePath = STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{filename}";
        io.print(STR."file path: \{filePath}");

        try {
            Files.delete(Path.of(filePath));
            var response = STR."File \{filename} deleted by user \{username}";
            transport.send(new DeleteFileResponse(STR."\{response}\n"));
            io.println(response);
        } catch (IOException e) {
            throw new CommandException(STR."File '\{filename}' doesn't exist");
        }
    }
}
