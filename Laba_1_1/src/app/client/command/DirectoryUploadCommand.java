package app.client.command;

import app.IO;
import app.client.TokenHolder;
import app.server.filestorage.FileStorageException;
import app.transport.Transport;
import app.transport.message.storage.DirectoryUploadRequest;
import app.transport.message.storage.DirectoryUploadResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DirectoryUploadCommand extends Command {

    public final TokenHolder tokenHolder;

    public DirectoryUploadCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() {
        io.print("enter directory name: ");
        var directoryName = io.readln();

        var directoryPath = Path.of(directoryName);
        if (!Files.exists(directoryPath)) {
            throw new CommandException("Directory doesn't exist");
        }

        transport.send(new DirectoryUploadRequest(tokenHolder.getToken(), directoryName));

        try (var files = Files.list(directoryPath)) {
            List<String> listOfFiles = files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();

            for (var file : listOfFiles) {
                var filePath = Path.of(directoryName, file);
                var fileSize = Files.size(filePath);

                try (var fileInputStream = Files.newInputStream(filePath)) {
                    var transportOutputStream = transport.getOutputStream();
                    var transferred = fileInputStream.transferTo(transportOutputStream);
                    transportOutputStream.flush();
                    assert transferred == fileSize;
                } catch (Exception e) {
                    throw new CommandException(e);
                }
            }
        } catch (Exception e) {
            throw new FileStorageException(e);
        }

        var response = expectMessage(DirectoryUploadResponse.class);
        io.print(response.getResponse());

    }

}
