package com.challenge.jorgebarreto.notifications.core.application.decorator;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RetryNotificationChannel<T extends Notification> implements NotificationChannel<T> {

    private final NotificationChannel<T> delegate;
    private final int maxRetries;

    @Override
    public NotificationResult send(T notification) {

        if (maxRetries < 0) {
            throw new SendNotificationException("Retries must be >= 0");
        }

        int attempt = 0;
        NotificationResult result;

        do {
            attempt++;

            result = delegate.send(notification);

            if (result.isSuccess()) {
                return result;
            }

            if (!isRetryable(result)) {
                log.warn(
                        "Non-retryable error for recipient={} code={} message={}",
                        notification.recipient(),
                        result.getErrorCode(),
                        result.getMessage()
                );
                return result;
            }

            log.warn(
                    "Retry attempt {} failed for recipient={} code={} message={}",
                    attempt,
                    notification.recipient(),
                    result.getErrorCode(),
                    result.getMessage()
            );

        } while (attempt <= maxRetries);

        return result;
    }

    private boolean isRetryable(NotificationResult result) {
        return switch (result.getErrorCode()) {
            case "VALIDATION", "CONFIGURATION" -> false;
            default -> true;
        };
    }
}