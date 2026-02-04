package com.challenge.jorgebarreto.notifications.core.domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.model.Notification;

public interface NotificationValidator<T extends Notification> {

    void validate(T  notification);
}
