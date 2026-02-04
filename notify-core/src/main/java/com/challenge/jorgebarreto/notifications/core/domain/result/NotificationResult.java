package com.challenge.jorgebarreto.notifications.core.domain.result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class NotificationResult {

    boolean success;
    String errorCode;
    String message;
    Throwable cause;
    LocalDateTime timestamp;

    public static NotificationResult success() {
        return new NotificationResult(true, null, "OK", null, LocalDateTime.now());
    }

    public static NotificationResult failure(String code, String message, Throwable cause) {
        return new NotificationResult(false, code, message, cause, LocalDateTime.now());
    }
}