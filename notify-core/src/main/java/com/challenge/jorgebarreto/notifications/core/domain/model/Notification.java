package com.challenge.jorgebarreto.notifications.core.domain.model;

public sealed interface Notification permits EmailNotification, SmsNotification, PushNotification {
    String recipient();
    String message();
}