package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.input;

import com.challenge.jorgebarreto.notifications.core.application.listener.NotificationListener;
import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingNotificationListener implements NotificationListener {

    @Override
    public void onNotificationSent(
            Notification notification,
            NotificationResult result
    ) {
        log.info(
                "EVENT -> recipient=" + notification.recipient() +
                " success=" + result.isSuccess() +
                " code=" + result.getErrorCode()
        );
    }
}