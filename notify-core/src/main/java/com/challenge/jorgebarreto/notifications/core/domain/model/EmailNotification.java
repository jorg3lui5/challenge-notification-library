package com.challenge.jorgebarreto.notifications.core.domain.model;

import java.util.Map;

public record EmailNotification(
        String recipient,
        String subject,
        String message,
        Map<String, Object> attributes
) implements Notification {}