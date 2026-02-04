package com.challenge.jorgebarreto.notifications.core.application.composite;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.CompositeNotificationResult;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CompositeNotificationChannel<T extends Notification> implements NotificationChannel<T> {

    private final List<NotificationChannel<T>> delegates;

    @Override
    public NotificationResult send(T notification) {
        List<NotificationResult> results = new ArrayList<>();

        for (NotificationChannel<T> sender : delegates) {
            try {
                results.add(sender.send(notification));
            } catch (Exception e) {
                results.add(
                        NotificationResult.failure(
                                "TECHNICAL_ERROR",
                                e.getMessage(),
                                e
                        )
                );
            }
        }
        boolean allSuccess = results.stream().allMatch(NotificationResult::isSuccess);

        return allSuccess
                ? CompositeNotificationResult.success(results)
                : CompositeNotificationResult.partialFailure(results);
    }
}
