package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.sms;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.application.port.output.security.CredentialsProvider;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TwilioSmsProvider implements NotificationProviderPort<SmsNotification> {

    private final CredentialsProvider credentials;

    @Override
    public ProviderResult send(SmsNotification notification) {
        log.info("Sending SMS to {}", notification.recipient());
        String token = credentials.get("TWILIO_API_TOKEN");
        log.info("Simulating Mailgun EMAIL with token present={}", token != null);
        if (notification.message().contains("FAIL")) {
            return ProviderResult.failure(
                    "PROVIDER",
                    "Twilio SMS failed",
                    null
            );
        }
        return ProviderResult.ok();
    }
}
