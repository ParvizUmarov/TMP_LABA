package app.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public record Barber(Integer id, String name, String surname, String birthday, String phone, String mail,
                     String password, Boolean authState, Integer workExperience) {
}
