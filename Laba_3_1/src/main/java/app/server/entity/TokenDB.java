package app.server.entity;

public class TokenDB {
    private final int id;
    private final String mail;
    private final String token;


    public TokenDB(int id, String mail, String token) {
        this.id = id;
        this.mail = mail;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getToken() {
        return token;
    }
}
