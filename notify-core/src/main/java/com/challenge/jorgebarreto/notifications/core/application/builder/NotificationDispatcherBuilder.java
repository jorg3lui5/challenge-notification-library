package com.challenge.jorgebarreto.notifications.core.application.builder;

import com.challenge.jorgebarreto.notifications.core.application.decorator.RetryNotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.listener.NotificationListener;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationEventPublisher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.publisher.InMemoryNotificationEventPublisher;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.publisher.NoOpNotificationEventPublisher;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationDispatcherBuilder {

    private final NotificationChannelRegistry registry =
            new NotificationChannelRegistry();

    private NotificationEventPublisher eventPublisher =
            new NoOpNotificationEventPublisher();

    private int retryAttempts = 0;

    private NotificationDispatcherBuilder() {}

    public static NotificationDispatcherBuilder builder() {
        return new NotificationDispatcherBuilder();
    }

    public NotificationDispatcherBuilder withRetry(int retryAttempts) {
        if (retryAttempts < 0) {
            throw new ValidationException("retryAttempts must be >= 0");
        }
        this.retryAttempts = retryAttempts;
        return this;
    }

    public NotificationDispatcherBuilder withEventPublisher(
            NotificationEventPublisher publisher
    ) {
        this.eventPublisher = publisher;
        return this;
    }

    public NotificationDispatcherBuilder addListener(
            NotificationListener listener
    ) {
        if (eventPublisher instanceof InMemoryNotificationEventPublisher pub) {
            pub.subscribe(listener);
        }
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
        return new NotificationDispatcher(registry, eventPublisher);
    }
}
