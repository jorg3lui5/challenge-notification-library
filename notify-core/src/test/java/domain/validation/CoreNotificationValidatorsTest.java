package domain.validation;

import com.challenge.jorgebarreto.notifications.core.domain.validation.CoreNotificationValidators;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoreNotificationValidatorsTest {

    @Test
    void shouldFailWhenRecipientIsNull() {
        EmailNotification n =
                new EmailNotification(null, "sub", "msg", Map.of());

        assertThrows(
                ValidationException.class,
                () -> CoreNotificationValidators.validateRequiredFields(n)
        );
    }

    @Test
    void shouldFailWhenMessageIsBlank() {
        SmsNotification n =
                new SmsNotification("+5939999999", " ");

        assertThrows(
                ValidationException.class,
                () -> CoreNotificationValidators.validateRequiredFields(n)
        );
    }

    @Test
    void shouldPassWhenRequiredFieldsPresent() {
        SmsNotification n =
                new SmsNotification("+5939999999", "hello");

        assertDoesNotThrow(
                () -> CoreNotificationValidators.validateRequiredFields(n)
        );
    }
}
