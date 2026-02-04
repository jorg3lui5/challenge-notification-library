package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.push;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.model.PushNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OneSignalPushProvider implements NotificationProviderPort<PushNotification> {

    private final String apiKey;
    private final String appId;

    @Override
    public ProviderResult send(PushNotification notification) {
        log.info("Sending PUSH via OneSignal to {}", notification.recipient());

        // Simulación de error real
        if (notification.message().contains("FAIL")) {
            return ProviderResult.failure(
                    "PROVIDER",
                    "OneSignal failed to send push notification",
                    null
            );
        }
        log.info("Push notification successfully sent via OneSignal");
        return ProviderResult.ok();
    }
}
