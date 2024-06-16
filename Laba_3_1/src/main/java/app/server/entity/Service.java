package app.server.entity;


import app.transport.message.Message;

public class Service extends Message {
    private final int id;
    private final String name;
    private final int price;


    public Service(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
