package com.challenge.jorgebarreto.notifications.core.application.registry;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

import java.util.HashMap;
import java.util.Map;

public class NotificationChannelRegistry {

    private final Map<Class<? extends Notification>, NotificationChannel<?>> channels = new HashMap<>();

    public <T extends Notification> void register(Class<T> type, NotificationChannel<T> channel) {
        channels.put(type, channel);
    }

    @SuppressWarnings("unchecked")
    public <T extends Notification> NotificationChannel<T> resolve(
            Class<? extends Notification> type
    ) {
        NotificationChannel<?> channel = channels.get(type);

        if (channel == null) {
            throw new ValidationException(
                    "No channel registered for " + type.getSimpleName()
            );
        }

        return (NotificationChannel<T>) channel;
    }
}