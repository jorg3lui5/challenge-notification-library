package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.publisher;

import com.challenge.jorgebarreto.notifications.core.application.listener.NotificationListener;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationEventPublisher;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotificationEventPublisher implements NotificationEventPublisher {

    private final List<NotificationListener> listeners = new ArrayList<>();

    public void subscribe(NotificationListener listener) {
        listeners.add(listener);
    }

    public void publish(Notification notification, NotificationResult result) {
        for (NotificationListener listener : listeners) {
            listener.onNotificationSent(notification, result);
        }
    }
}
