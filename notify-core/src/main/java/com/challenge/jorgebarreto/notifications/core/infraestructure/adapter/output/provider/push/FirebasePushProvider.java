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
public class FirebasePushProvider implements NotificationProviderPort<PushNotification> {

    private final String credentials;

    @Override
    public ProviderResult send(PushNotification notification) {
        log.info("Sending PUSH notification to {}", notification.recipient());

        if (notification.message().contains("FAIL")) {
            return ProviderResult.failure(
                    "PROVIDER",
                    "Firebase Push failed",
                    null
            );
        }
        return ProviderResult.ok();
    }
}
