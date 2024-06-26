package app.server.net;

import app.IO;
import app.Settings;
import app.server.ServerException;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.FileUploadRequest;
import app.transport.message.storage.FileUploadResponse;
import app.transport.message.storage.FileUploadRewriteConfirmation;

import java.io.File;

public class FileUploadHandler extends Handler {
    private final FileStorageService fileSystemService;
    private final SessionService sessionService;
    private final long MAX_SIZE = 10_000;

    public FileUploadHandler(Transport transport, IO io, FileStorageService fileStorageService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileStorageService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileUploadRequest) message;
        var fileSize = req.getSize();
        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);
        var subDir = req.getSubDir();

        File file = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{subDir}");
        file.mkdir();

        var filename = subDir + "/" + req.getFilename();
        var fileExists = fileSystemService.fileExists(username, filename);

        if(fileSize > MAX_SIZE){
            throw new ServerException(STR."File size is bigger than '\{MAX_SIZE}' bytes ");
        }

        transport.send(new FileUploadResponse(fileExists, "file upload success"));
        if (fileExists) {
            transport.receive(FileUploadRewriteConfirmation.class);
        }

        try (var fileOutputStream = fileSystemService.getFileOutputStream(username, filename)) {
            var transpotInputStream = transport.getInputStream();
            var transferred = transpotInputStream.transferTo(fileOutputStream);
            fileOutputStream.flush();
            assert transferred == req.getSize();
        } catch (Exception e) {
            throw new ServerException(e);
        }

        io.println(STR."file '\{filename}' uploaded by user '\{username}'");
    }
}
