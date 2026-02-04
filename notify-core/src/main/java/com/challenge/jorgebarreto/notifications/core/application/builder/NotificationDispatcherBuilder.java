package com.challenge.jorgebarreto.notifications.core.application.builder;

import com.challenge.jorgebarreto.notifications.core.application.decorator.RetryNotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

public class NotificationDispatcherBuilder {

    private final NotificationChannelRegistry registry =
            new NotificationChannelRegistry();

    private int retryAttempts = 0;

    private NotificationDispatcherBuilder() {}

    public static NotificationDispatcherBuilder builder() {
        return new NotificationDispatcherBuilder();
    }

    public NotificationDispatcherBuilder withRetry(int retryAttempts) {
        if (retryAttempts < 0) {
            throw new IllegalArgumentException("retryAttempts must be >= 0");
        }
        this.retryAttempts = retryAttempts;
        return this;
    }

    public <T extends Notification> NotificationDispatcherBuilder registerChannel(
            Class<T> type,
            NotificationChannel<T> channel
    ) {

        NotificationChannel<T> finalChannel = channel;

        if (retryAttempts > 0) {
            finalChannel = new RetryNotificationChannel<>(channel, retryAttempts);
        }

        registry.register(type, finalChannel);
        return this;
    }

    public NotificationDispatcher build() {
        return new NotificationDispatcher(registry);
    }
}
