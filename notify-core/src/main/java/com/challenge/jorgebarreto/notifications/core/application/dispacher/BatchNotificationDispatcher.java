package com.challenge.jorgebarreto.notifications.core.application.dispacher;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BatchNotificationDispatcher {

    private final AsyncNotificationDispatcher asyncDispatcher;

    public CompletableFuture<List<NotificationResult>> sendBatchAsync(
            List<? extends Notification> notifications
    ) {
        if (notifications == null || notifications.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }

        List<CompletableFuture<NotificationResult>> futures =
                notifications.stream()
                        .map(asyncDispatcher::sendAsync)
                        .toList();

        return CompletableFuture
                .allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v ->
                        futures.stream()
                                .map(CompletableFuture::join)
                                .toList()
                );
    }
}
