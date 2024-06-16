package app.transport.message.storage;

import app.transport.message.Message;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterCustomerRequest extends Message {
    private final String username;
    private final String surname;
    private final String birthday;
    private final String phone;
    private final String mail;
    private final String password;

    public RegisterCustomerRequest(String username, String surname, String birthday, String phone, String mail, String password) {
        this.username = username;
        this.surname = surname;
        this.birthday = birthday;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

   
}
