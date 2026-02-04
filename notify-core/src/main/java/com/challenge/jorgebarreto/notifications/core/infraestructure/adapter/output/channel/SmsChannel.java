package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.validation.CoreNotificationValidators;
import com.challenge.jorgebarreto.notifications.core.domain.validation.NotificationValidator;
import com.challenge.jorgebarreto.notifications.core.domain.validation.PhoneValidator;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SmsChannel implements NotificationChannel<SmsNotification> {

    private final NotificationProviderPort<SmsNotification> provider;
    private final List<NotificationValidator<SmsNotification>> customValidators;

    @Override
    public NotificationResult send(SmsNotification notification) {
        try {
            //Validaciones BASE
            CoreNotificationValidators.validateRequiredFields(notification);
            //Validación específica
            new PhoneValidator().validate(notification);
            //Custom
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