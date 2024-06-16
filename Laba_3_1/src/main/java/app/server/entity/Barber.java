package app.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Barber(Integer id,
                     String name,
                     String surname,
                     String birthday,
                     String phone,
                     String mail,
                     String password,
                     Boolean authState,
                     Integer workExperience,
                     Integer salonId,
                     Integer serviceId) {}
