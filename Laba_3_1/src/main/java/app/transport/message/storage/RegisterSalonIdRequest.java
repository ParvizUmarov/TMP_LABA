package app.transport.message.storage;

import app.transport.message.Message;

public class RegisterSalonIdRequest extends Message {
    private final int salonId;

    public RegisterSalonIdRequest(int salonId) {
        this.salonId = salonId;
    }

    public int getSalonId() {
        return salonId;
    }
}
