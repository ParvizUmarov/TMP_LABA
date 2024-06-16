package app.server.session;

import app.server.user.UserType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private final Map<String, Object> map = new ConcurrentHashMap<>();
    public static final String MAIL = "mail";
    private LocalDateTime expiredAt;
    private UserType userType;

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    boolean isExpired(){
        LocalDateTime now = LocalDateTime.now();
        if(expiredAt != null){
            return now.isAfter(expiredAt);
        }
        return false;
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }
}

