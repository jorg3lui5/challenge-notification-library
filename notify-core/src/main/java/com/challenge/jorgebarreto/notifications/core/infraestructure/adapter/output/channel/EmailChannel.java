package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.validation.CoreNotificationValidators;
import com.challenge.jorgebarreto.notifications.core.domain.validation.EmailValidator;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.validation.NotificationValidator;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EmailChannel implements NotificationChannel<EmailNotification> {

    private final NotificationProviderPort<EmailNotification> provider;
    private final List<NotificationValidator<EmailNotification>> customValidators;

    @Override
    public NotificationResult send(EmailNotification notification) {
        try {
            //Validaciones BASE
            CoreNotificationValidators.validateRequiredFields(notification);
            //Validaciones propias del canal
            new EmailValidator().validate(notification);
            //Validaciones custom del usuario
            customValidators.forEach(v -> v.validate(notification));
            provider.send(notification);
            return NotificationResult.success();

        } catch (ValidationException e) {
            return NotificationResult.failure("VALIDATION", e.getMessage(), e);

        } catch (SendNotificationException e) {
            return NotificationResult.failure("PROVIDER", e.getMessage(), e);
        }
    }
}
