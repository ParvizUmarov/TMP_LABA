package app.transport.message.storage;

import app.transport.message.Message;

public class RegisterServiceIdRequest extends Message {
    private final int serviceID;

    public RegisterServiceIdRequest(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getServiceID() {
        return serviceID;
    }
}
