package com.challenge.jorgebarreto.notifications.core.domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;

import java.util.regex.Pattern;

public class EmailValidator implements NotificationValidator<EmailNotification> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void validate(EmailNotification notification) {
        if (!EMAIL_PATTERN.matcher(notification.recipient()).matches()) {
            throw new ValidationException("Invalid email");
        }
    }
}
