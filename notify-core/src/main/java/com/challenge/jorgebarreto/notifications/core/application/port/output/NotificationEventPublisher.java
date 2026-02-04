package com.challenge.jorgebarreto.notifications.core.application.port.output;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;

public interface NotificationEventPublisher {

    void publish(Notification notification, NotificationResult result);
}