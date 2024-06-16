package app.server.entity;

import app.transport.message.Message;

public class Order extends Message {
    private final int id;
    private final int barber_id;
    private final int customer_id;
    private final String status;
    private final String time;
    private final int grade;

    public Order(int id, int barberId, int customerId, String status, String time, int grade) {
        this.id = id;
        barber_id = barberId;
        customer_id = customerId;
        this.status = status;
        this.time = time;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getBarber_id() {
        return barber_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public int getGrade() {
        return grade;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
