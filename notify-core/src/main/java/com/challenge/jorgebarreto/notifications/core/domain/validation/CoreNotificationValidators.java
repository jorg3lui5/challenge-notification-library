package com.challenge.jorgebarreto.notifications.core.domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

public final class CoreNotificationValidators {

    private CoreNotificationValidators() {}

    public static <T extends Notification> void validateRequiredFields(T notification) {

        if (notification.recipient() == null || notification.recipient().isBlank()) {
            throw new ValidationException("Recipient is required");
        }

        if (notification.message() == null || notification.message().isBlank()) {
            throw new ValidationException("Message is required");
        }
    }
}
