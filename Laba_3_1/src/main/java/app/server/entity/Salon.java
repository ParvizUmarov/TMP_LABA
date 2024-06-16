package app.server.entity;

import app.transport.message.Message;


public class Salon extends Message{
    private final int id;
    private final String address;
    private final String images;


    public Salon(int id, String address, String images) {
        this.id = id;
        this.address = address;
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getImages() {
        return images;
    }
}
