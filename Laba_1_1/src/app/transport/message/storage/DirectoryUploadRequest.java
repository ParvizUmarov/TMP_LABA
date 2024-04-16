package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class DirectoryUploadRequest extends AuthorizedMessage {

    private final String directoryName;

    public DirectoryUploadRequest(String authToken, String directoryName) {
        super(authToken);
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
