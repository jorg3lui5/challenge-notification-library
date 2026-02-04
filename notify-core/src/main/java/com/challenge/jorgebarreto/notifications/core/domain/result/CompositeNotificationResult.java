package com.challenge.jorgebarreto.notifications.core.domain.result;

import java.time.LocalDateTime;
import java.util.List;

public final class CompositeNotificationResult extends NotificationResult {

    private final List<NotificationResult> results;

    private CompositeNotificationResult(
            boolean success,
            String errorCode,
            String message,
            Throwable cause,
            List<NotificationResult> results
    ) {
        super(success, errorCode, message, cause, LocalDateTime.now());
        this.results = results;
    }

    public static CompositeNotificationResult success(List<NotificationResult> results) {
        return new CompositeNotificationResult(
                true, null, "ALL_CHANNELS_SUCCESS", null, results
        );
    }

    public static CompositeNotificationResult partialFailure(List<NotificationResult> results) {
        return new CompositeNotificationResult(
                false, "PARTIAL_FAILURE",
                "One or more channels failed",
                null,
                results
        );
    }

    public List<NotificationResult> results() {
        return results;
    }
}
