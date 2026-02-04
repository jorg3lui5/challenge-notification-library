package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.template.MessageTemplateEngine;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import com.challenge.jorgebarreto.notifications.core.domain.validation.EmailValidator;
import com.challenge.jorgebarreto.notifications.core.domain.exception.SendNotificationException;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.validation.NotificationValidator;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import com.challenge.jorgebarreto.notifications.core.domain.validation.RequiredFieldsValidator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EmailChannel implements NotificationChannel<EmailNotification> {

    private final NotificationProviderPort<EmailNotification> provider;
    private final List<NotificationValidator<EmailNotification>> customValidators;
    private final MessageTemplateEngine templateEngine = new MessageTemplateEngine();
    private static final List<NotificationValidator<EmailNotification>> INTERNAL_VALIDATORS =
            List.of(
                    new RequiredFieldsValidator<>(),
                    new EmailValidator()
            );

    @Override
    public NotificationResult send(EmailNotification notification) {
        try {
            //Validaciones BASE
            INTERNAL_VALIDATORS.forEach(v -> v.validate(notification));

            //Validaciones custom del usuario
            customValidators.forEach(v -> v.validate(notification));

            // 🎯 APLICACIÓN DEL TEMPLATE
            String renderedMessage =
                    templateEngine.render(
                            notification.message(),
                            notification.attributes()
                    );

            EmailNotification rendered =
                    new EmailNotification(
                            notification.recipient(),
                            notification.subject(),
                            renderedMessage,
                            notification.attributes()
                    );

            ProviderResult providerResult = provider.send(rendered);

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
