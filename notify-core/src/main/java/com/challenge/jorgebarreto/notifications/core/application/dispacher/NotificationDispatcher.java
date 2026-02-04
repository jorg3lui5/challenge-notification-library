package com.challenge.jorgebarreto.notifications.core.application.dispacher;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationDispatcher {

    private final NotificationChannelRegistry registry;

    public NotificationResult send(Notification notification) {

        if (notification == null) {
            return NotificationResult.failure(
                    "CORE_VALIDATION",
                    "Notification must not be null",
                    null
            );
        }

        try {
            NotificationChannel<Notification> channel = registry.resolve(notification.getClass());

            return channel.send(notification);

        } catch (ValidationException e) {
            return NotificationResult.failure(
                    "CONFIGURATION",
                    e.getMessage(),
                    e
            );
        }
    }
}