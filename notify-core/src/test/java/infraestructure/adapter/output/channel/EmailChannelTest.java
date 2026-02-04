package infraestructure.adapter.output.channel;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationProviderPort;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import com.challenge.jorgebarreto.notifications.core.domain.result.ProviderResult;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel.EmailChannel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmailChannelTest {

    @Test
    void shouldSendEmailSuccessfully() {
        NotificationProviderPort<EmailNotification> provider =
                n -> ProviderResult.ok();

        EmailChannel channel =
                new EmailChannel(provider, List.of());

        EmailNotification notification =
                new EmailNotification(
                        "test@mail.com",
                        "subject",
                        "message",
                        Map.of()
                );

        NotificationResult result = channel.send(notification);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldFailOnInvalidEmail() {
        NotificationProviderPort<EmailNotification> provider =
                n -> ProviderResult.ok();

        EmailChannel channel =
                new EmailChannel(provider, List.of());

        EmailNotification notification =
                new EmailNotification(
                        "invalid-email",
                        "subject",
                        "message",
                        Map.of()
                );

        NotificationResult result = channel.send(notification);

        assertFalse(result.isSuccess());
        assertEquals("VALIDATION", result.getErrorCode());
    }
}
