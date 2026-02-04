package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.publisher;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationEventPublisher;
import com.challenge.jorgebarreto.notifications.core.domain.dto.NotificationEvent;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaNotificationEventPublisher implements NotificationEventPublisher {

    @Override
    public void publish(Notification notification, NotificationResult result) {

        NotificationEvent event =
                new NotificationEvent(notification, result);

        log.info(
                "[KAFKA] topic=notification-events payload={}",
                event
        );
    }
}
