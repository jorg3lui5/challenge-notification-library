package com.challenge.jorgebarreto.notifications.core.domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.PushNotification;

public class PushTokenValidator implements NotificationValidator<PushNotification> {

    @Override
    public void validate(PushNotification notification) {
        if (notification.recipient() == null ||
            notification.recipient().length() < 10) {
            throw new ValidationException("Invalid push token");
        }
    }
}
