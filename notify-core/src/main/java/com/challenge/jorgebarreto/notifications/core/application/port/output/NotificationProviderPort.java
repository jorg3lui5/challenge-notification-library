package com.challenge.jorgebarreto.notifications.core.application.port.output;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;

public interface NotificationProviderPort<T extends Notification> {
    ProviderResult send(T notification);
}
