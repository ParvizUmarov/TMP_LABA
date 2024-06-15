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
        var request = (DirectoryUploadRequest) message;

        var username = sessionService.get(Token.fromText(request.getAuthToken())).getString(Session.USERNAME);
        var directoryUploadPath = getDirectoryUploadPath(request, username);

        System.out.println("dir path " + directoryUploadPath);

        int counter = 0;
        while (request.getFileCount() != counter) {
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

    private static String getDirectoryUploadPath(DirectoryUploadRequest request, String username) {
        var directoryPath = Path.of(request.getDirectoryName());

        var subdirectoryName = request.getSubdirectoryName();
        File subDir = new File(STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{subdirectoryName}");

        subDir.mkdir();

        var dirName = Path.of(String.valueOf(directoryPath)).getFileName();
        var subDirPath = STR."\{Settings.SERVER_FILE_STORAGE_BASE_PATH}/\{username}/\{subdirectoryName}/\{dirName}";

        File dir = new File(subDirPath);
        if (dir.exists()) {
            throw new ServerException("Directory is exist");
        }

        dir.mkdir();

        var path = Path.of(subdirectoryName);

        var subDirName = Path.of(String.valueOf(path)).getFileName();
        var directoryUploadPath = "";
        if (subdirectoryName.isEmpty()) {
            directoryUploadPath = Path.of(username, String.valueOf(dirName)).toString();
        }else{
            directoryUploadPath = Path.of(username, String.valueOf(subDirName), String.valueOf(dirName)).toString();
        }
        return directoryUploadPath;
    }
}

