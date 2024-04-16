package app.server.net;

import app.IO;
import app.Settings;
import app.server.ServerException;
import app.server.filestorage.FileStorageException;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.DirectoryUploadRequest;
import app.transport.message.storage.DirectoryUploadResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DirectoryUploadHandler extends Handler {

    private final SessionService sessionService;
    private final FileStorageService fileSystemService;

    public DirectoryUploadHandler(Transport transport, IO io, SessionService sessionService, FileStorageService fileSystemService) {
        super(transport, io);
        this.sessionService = sessionService;
        this.fileSystemService = fileSystemService;
    }

    @Override
    public void handle(Message message) {
        var req = (DirectoryUploadRequest) message;

        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);
        var directoryName = req.getDirectoryName();
        var directoryPath = Path.of(directoryName);

        var dirName = Path.of(String.valueOf(directoryPath)).getFileName();

        try (var files = Files.list(directoryPath)) {
            List<String> list = files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();

            File dir = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{dirName}");
            if (dir.exists()) {
                throw new ServerException("Directory is exist");
            }

            dir.mkdir();
            var directoryUploadPath = Path.of(username, String.valueOf(dirName)).toString();

            for (var file : list) {
                try (var fileOutputStream = fileSystemService.getDirOutputStream(directoryUploadPath, file)) {
                    var transpotInputStream = transport.getInputStream();
                    transpotInputStream.transferTo(fileOutputStream);
                    fileOutputStream.flush();
                } catch (Exception e) {
                    throw new ServerException(e);
                }
            }
            transport.send(new DirectoryUploadResponse("Directory is uploaded success !!!"));
        } catch (Exception e) {
            throw new FileStorageException(e);
        }
    }
}
