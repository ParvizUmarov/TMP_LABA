package org.example.message;

import java.io.IOException;

public interface ResponseListener {
    void onResponseReceived(Message response) throws IOException;
    void onError(Throwable error);
}
