package com.challenge.jorgebarreto.notifications.core.application.dispacher;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
public class AsyncNotificationDispatcher {

    private final NotificationDispatcher dispatcher;
    private final Executor executor;

    public CompletableFuture<NotificationResult> sendAsync(Notification notification) {
        return CompletableFuture.supplyAsync(
                () -> dispatcher.send(notification),
                executor
        ).exceptionally(e -> NotificationResult.failure("ASYNC_ERROR", e.getMessage(), e));
    }
}
