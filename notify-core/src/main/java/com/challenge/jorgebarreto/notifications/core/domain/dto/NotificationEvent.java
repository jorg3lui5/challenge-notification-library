package com.challenge.jorgebarreto.notifications.core.domain.dto;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class NotificationEvent {

    private final String eventId;
    private final Instant occurredAt;

    private final String notificationType;
    private final String recipient;

    private final boolean success;
    private final String errorCode;
    private final String errorMessage;

    public NotificationEvent(
            Notification notification,
            NotificationResult result
    ) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();

        this.notificationType =
                notification != null
                        ? notification.getClass().getSimpleName()
                        : "UNKNOWN";

        this.recipient =
                notification != null
                        ? notification.recipient()
                        : null;

        this.success = result.isSuccess();
        this.errorCode = result.getErrorCode();
        this.errorMessage = result.getMessage();
    }
}
