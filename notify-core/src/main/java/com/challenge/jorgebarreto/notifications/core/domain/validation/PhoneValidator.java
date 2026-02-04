package com.challenge.jorgebarreto.notifications.core.domain.validation;


import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;

public class PhoneValidator implements NotificationValidator<SmsNotification> {

    @Override
    public void validate(SmsNotification notification) {
        String phone = notification.recipient();

        if (phone == null || !phone.matches("^\\+?[1-9]\\d{7,14}$")) {
            throw new ValidationException("Invalid phone number");
        }
    }
}
