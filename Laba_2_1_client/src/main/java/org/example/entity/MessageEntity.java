package org.example.entity;

import java.time.LocalDateTime;

public record MessageEntity(String username, String message, LocalDateTime time) {
}
