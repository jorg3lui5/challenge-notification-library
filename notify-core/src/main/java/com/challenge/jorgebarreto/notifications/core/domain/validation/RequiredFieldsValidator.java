package com.challenge.jorgebarreto.notifications.core.domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

public class RequiredFieldsValidator<T extends Notification>
        implements NotificationValidator<T> {

    @Override
    public void validate(T notification) {
        if (notification.recipient() == null || notification.recipient().isBlank()) {
            throw new ValidationException("Recipient is required");
        }
        if (notification.message() == null || notification.message().isBlank()) {
            throw new ValidationException("Message is required");
        }
    }
}