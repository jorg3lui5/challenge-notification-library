package application.composite;

import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.composite.CompositeNotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.result.CompositeNotificationResult;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompositeNotificationChannelTest {

    @Test
    void shouldReturnSuccessWhenAllChannelsSucceed() {
        NotificationChannel<SmsNotification> ok =
                n -> NotificationResult.success();

        CompositeNotificationChannel<SmsNotification> composite =
                new CompositeNotificationChannel<>(List.of(ok, ok));

        NotificationResult result =
                composite.send(new SmsNotification("+5939", "hi"));

        assertTrue(result.isSuccess());
        assertTrue(result instanceof CompositeNotificationResult);
    }

    @Test
    void shouldReturnPartialFailureWhenOneFails() {
        NotificationChannel<SmsNotification> ok =
                n -> NotificationResult.success();

        NotificationChannel<SmsNotification> fail =
                n -> NotificationResult.failure("PROVIDER", "fail", null);

        CompositeNotificationChannel<SmsNotification> composite =
                new CompositeNotificationChannel<>(List.of(ok, fail));

        NotificationResult result =
                composite.send(new SmsNotification("+5939", "hi"));

        assertFalse(result.isSuccess());
        assertEquals("PARTIAL_FAILURE", result.getErrorCode());
    }
}
