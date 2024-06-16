package app.client;

public class TokenHolder {
    private String token;
    private String userType;
    private String userMail;

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}
