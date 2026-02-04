package com.challenge.jorgebarreto.notifications.core.application.listener;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;

public interface NotificationListener {

    void onNotificationSent(
            Notification notification,
            NotificationResult result
    );
}