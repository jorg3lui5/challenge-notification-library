package application.dispacher;

import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel.SmsChannel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDispatcherTest {

    @Test
    void shouldDispatchCorrectChannel() {
        NotificationChannelRegistry registry =
                new NotificationChannelRegistry();

        registry.register(
                SmsNotification.class,
                n -> NotificationResult.success()
        );

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(registry);

        NotificationResult result =
                dispatcher.send(new SmsNotification("+593", "hello"));

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldFailOnNullNotification() {
        NotificationDispatcher dispatcher =
                new NotificationDispatcher(new NotificationChannelRegistry());

        NotificationResult result = dispatcher.send(null);

        assertFalse(result.isSuccess());
        assertEquals("CORE_VALIDATION", result.getErrorCode());
    }

    @Test
    void shouldFailWhenNoChannelRegistered() {

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(new NotificationChannelRegistry());

        NotificationResult result =
                dispatcher.send(new SmsNotification("+593", "hi"));

        assertFalse(result.isSuccess());
        assertEquals("CONFIGURATION", result.getErrorCode());
    }

    @Test
    void shouldFailWhenValidationFails() {

        NotificationChannelRegistry registry =
                new NotificationChannelRegistry();

        SmsChannel smsChannel =
                new SmsChannel(
                        notification -> {
                            // no hace nada
                        },
                        List.of()
                );

        registry.register(SmsNotification.class, smsChannel);

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(registry);

        // Teléfono inválido
        NotificationResult result =
                dispatcher.send(
                        new SmsNotification("123", "hello")
                );

        assertFalse(result.isSuccess());
        assertEquals("VALIDATION", result.getErrorCode());
    }

}
