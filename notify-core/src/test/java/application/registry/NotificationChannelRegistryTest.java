package application.registry;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.exception.ValidationException;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationChannelRegistryTest {

    @Test
    void shouldResolveRegisteredChannel() {
        NotificationChannelRegistry registry = new NotificationChannelRegistry();

        NotificationChannel<EmailNotification> channel =
                n -> NotificationResult.success();

        registry.register(EmailNotification.class, channel);

        NotificationChannel<EmailNotification> resolved =
                registry.resolve(EmailNotification.class);

        assertNotNull(resolved);
    }

    @Test
    void shouldFailWhenNoChannelRegistered() {
        NotificationChannelRegistry registry = new NotificationChannelRegistry();

        assertThrows(
                ValidationException.class,
                () -> registry.resolve(EmailNotification.class)
        );
    }
}
