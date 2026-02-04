package com.challenge.jorgebarreto.notifications.core.domain.model;

public record SmsNotification(
        String recipient,
        String message
) implements Notification {}