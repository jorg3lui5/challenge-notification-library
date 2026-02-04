package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.email;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SendGridEmailProvider implements NotificationProviderPort<EmailNotification> {

    private final String apiKey;

    @Override
    public void send(EmailNotification notification) {
        log.info("Sending EMAIL to {}", notification.recipient());

        if (notification.message().contains("FAIL")) {
            throw new SendNotificationException("SendGrid failure");
        }
    }
}