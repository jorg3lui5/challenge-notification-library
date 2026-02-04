package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.sms;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NexmoSmsProvider implements NotificationProviderPort<SmsNotification> {

    private final String apiKey;
    private final String apiSecret;

    @Override
    public ProviderResult send(SmsNotification notification) {
        log.info("Sending SMS via NEXMO to {}", notification.recipient());

        // Simulación de fallo controlado
        if (notification.message().contains("FAIL")) {
            return ProviderResult.failure(
                    "PROVIDER",
                    "Nexmo failed to send SMS",
                    null
            );
        }

        log.info("SMS successfully sent via NEXMO");
        return ProviderResult.ok();
    }
}