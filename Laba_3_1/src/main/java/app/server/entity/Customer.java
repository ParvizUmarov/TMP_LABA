package app.server.entity;

import lombok.Getter;

@Getter
public record Customer(Integer id, String name, String surname, String birthday, String phone, String mail,
                     String password, Boolean authState) {
}
