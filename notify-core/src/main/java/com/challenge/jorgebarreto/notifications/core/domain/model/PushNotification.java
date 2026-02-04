package com.challenge.jorgebarreto.notifications.core.domain.model;

import java.util.Map;

public record PushNotification(
        String recipient,
        String title,
        String message,
        Map<String, String> data
) implements Notification {}
