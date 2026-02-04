package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.email;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailgunEmailProvider implements NotificationProviderPort<EmailNotification> {
private final String token;


    public MailgunEmailProvider(String token) {
        this.token = token;
    }

    @Override
    public void send(EmailNotification notification) {
        log.info("Simulating Mailgun EMAIL to {}", notification.recipient());
    }
}
