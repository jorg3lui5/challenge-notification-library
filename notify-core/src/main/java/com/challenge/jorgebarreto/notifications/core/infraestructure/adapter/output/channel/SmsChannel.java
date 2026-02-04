package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import com.challenge.jorgebarreto.notifications.core.domain.validation.NotificationValidator;
import com.challenge.jorgebarreto.notifications.core.domain.validation.PhoneValidator;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import com.challenge.jorgebarreto.notifications.core.domain.validation.RequiredFieldsValidator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SmsChannel implements NotificationChannel<SmsNotification> {

    private final NotificationProviderPort<SmsNotification> provider;
    private final List<NotificationValidator<SmsNotification>> customValidators;
    private static final List<NotificationValidator<SmsNotification>> INTERNAL_VALIDATORS =
            List.of(
                    new RequiredFieldsValidator<>(),
                    new PhoneValidator()
            );

    @Override
    public NotificationResult send(SmsNotification notification) {
        try {
            //Validaciones BASE
            INTERNAL_VALIDATORS.forEach(v -> v.validate(notification));
            //Custom
            customValidators.forEach(v -> v.validate(notification));

            ProviderResult providerResult = provider.send(notification);

            if (!providerResult.success()) {
                return NotificationResult.failure(
                        providerResult.errorCode(),
                        providerResult.message(),
                        providerResult.cause()
                );
            }

            return NotificationResult.success();

        } catch (ValidationException e) {
            return NotificationResult.failure("VALIDATION", e.getMessage(), e);

        } catch (SendNotificationException e) {
            return NotificationResult.failure("PROVIDER", e.getMessage(), e);
        }
    }
}