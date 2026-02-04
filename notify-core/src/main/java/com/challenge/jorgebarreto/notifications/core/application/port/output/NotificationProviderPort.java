package com.challenge.jorgebarreto.notifications.core.application.port.output;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

public interface NotificationProviderPort<T extends Notification> {
    void send(T notification);
}
