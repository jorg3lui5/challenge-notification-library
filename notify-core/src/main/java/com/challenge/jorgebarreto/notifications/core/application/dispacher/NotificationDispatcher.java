package com.challenge.jorgebarreto.notifications.core.application.dispacher;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationEventPublisher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationDispatcher {

    private final NotificationChannelRegistry registry;
    private final NotificationEventPublisher eventPublisher;

    public NotificationResult send(Notification notification) {

        if (notification == null) {
            return NotificationResult.failure(
                            "CORE_VALIDATION",
                            "Notification must not be null",
                            null
                    );
        }

        NotificationResult result;
        try {
            NotificationChannel<Notification> channel = registry.resolve(notification.getClass());

            result = channel.send(notification);

        } catch (ValidationException e) {
            result =
                    NotificationResult.failure(
                            "CONFIGURATION",
                            e.getMessage(),
                            e
                    );
        }
        // EVENTO PUBLICADO
        eventPublisher.publish(notification, result);

        return result;
    }
}