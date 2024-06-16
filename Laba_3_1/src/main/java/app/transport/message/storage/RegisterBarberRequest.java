package app.transport.message.storage;

import app.transport.message.Message;

public class RegisterBarberRequest extends Message {
    private final String username;
    private final String surname;
    private final String birthday;
    private final String phone;
    private final String mail;
    private final String password;
    private final int workExperience;

    public RegisterBarberRequest(String username, String surname, String birthday, String phone, String mail, String password, int workExperience) {
        this.username = username;
        this.surname = surname;
        this.birthday = birthday;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.workExperience = workExperience;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    @Override
    public String toString() {
        return "RegisterPasswordRequest{password="+password+"}";
    }
}
