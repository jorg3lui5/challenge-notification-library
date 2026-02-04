package application.decorator;

import com.challenge.jorgebarreto.notifications.core.application.decorator.RetryNotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.port.output.NotificationChannel;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RetryNotificationChannelTest {

    @Test
    void shouldRetryUntilSuccess() {
        AtomicInteger counter = new AtomicInteger();

        NotificationChannel<SmsNotification> delegate =
                n -> counter.incrementAndGet() < 3
                        ? NotificationResult.failure("PROVIDER", "fail", null)
                        : NotificationResult.success();

        RetryNotificationChannel<SmsNotification> retry =
                new RetryNotificationChannel<>(delegate, 3);

        NotificationResult result =
                retry.send(new SmsNotification("+5939999999", "msg"));

        assertTrue(result.isSuccess());
        assertEquals(3, counter.get());
    }

    @Test
    void shouldStopRetryOnValidationError() {
        NotificationChannel<SmsNotification> delegate =
                n -> NotificationResult.failure("VALIDATION", "bad", null);

        RetryNotificationChannel<SmsNotification> retry =
                new RetryNotificationChannel<>(delegate, 5);

        NotificationResult result =
                retry.send(new SmsNotification("+5939999999", "msg"));

        assertFalse(result.isSuccess());
        assertEquals("VALIDATION", result.getErrorCode());
    }

    @Test
    void shouldRetryOnlyOnProviderError() {
        NotificationChannel<SmsNotification> fail =
                n -> NotificationResult.failure("PROVIDER", "fail", null);

        RetryNotificationChannel<SmsNotification> retry =
                new RetryNotificationChannel<>(fail, 2);

        NotificationResult result =
                retry.send(new SmsNotification("+593999999", "hi"));

        assertFalse(result.isSuccess());
        assertEquals("PROVIDER", result.getErrorCode());
    }
}
