package com.challenge.jorgebarreto.notifications.core.application.service;

import com.challenge.jorgebarreto.notifications.core.application.dispacher.AsyncNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.BatchNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class NotificationClient {

    private final NotificationDispatcher dispatcher;
    private final AsyncNotificationDispatcher asyncDispatcher;
    private final BatchNotificationDispatcher batchDispatcher;

    public NotificationResult send(Notification notification) {
        return dispatcher.send(notification);
    }

    public CompletableFuture<NotificationResult> sendAsync(Notification notification) {
        return asyncDispatcher.sendAsync(notification);
    }

    public CompletableFuture<List<NotificationResult>> sendBatchAsync(
            List<? extends Notification> notifications
    ) {
        return batchDispatcher.sendBatchAsync(notifications);
    }
}
