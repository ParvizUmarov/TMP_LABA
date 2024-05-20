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
import app.transport.message.storage.*;

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
        var directoryUploadRequest = (DirectoryUploadRequest) message;

        var username = sessionService.get(Token.fromText(directoryUploadRequest.getAuthToken())).getString(Session.USERNAME);
        var directoryName = directoryUploadRequest.getDirectoryName();
        var directoryPath = Path.of(directoryName);

//        var subdirectoryName = directoryUploadRequest.getSubdirectoryName();
//        File subDir = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{subdirectoryName}");
//
//        subDir.mkdir();

        var dirName = Path.of(String.valueOf(directoryPath)).getFileName();

            File dir = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{dirName}");
            if (dir.exists()) {
                throw new ServerException("Directory is exist");
            }

            dir.mkdir();

            var directoryUploadPath = Path.of(username, String.valueOf(dirName)).toString();

            int counter = 0;
            while(directoryUploadRequest.getFileCount() != counter){
                counter++;
                System.out.println("file #" + counter);
                var fileUploadRequest = transport.receive(FileUploadRequest.class);
                var response = STR."file \{fileUploadRequest.getFilename()} is upload";
                transport.send(new FileUploadResponse(false, response));

                try (var fileOutputStream = fileSystemService.getDirOutputStream(directoryUploadPath, fileUploadRequest.getFilename())) {
                    var transpotInputStream = transport.getInputStream();
                    for (long i = 0; i < fileUploadRequest.getSize(); i++) {
                        int b = transpotInputStream.read();
                        fileOutputStream.write(b);
                    }
                    fileOutputStream.flush();
                } catch (Exception e) {
                    throw new ServerException(e);
                }

            }

        transport.send(new DirectoryUploadResponse("Directory successfully upload"));

    }
}

