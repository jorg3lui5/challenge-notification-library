package application.dispacher;

import com.challenge.jorgebarreto.notifications.core.application.dispacher.AsyncNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.BatchNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchNotificationDispatcherTest {

    @Test
    void shouldSendBatchAsync() {
        NotificationChannelRegistry registry =
                new NotificationChannelRegistry();

        registry.register(
                SmsNotification.class,
                n -> NotificationResult.success()
        );

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(registry);

        AsyncNotificationDispatcher async =
                new AsyncNotificationDispatcher(
                        dispatcher,
                        Executors.newFixedThreadPool(2)
                );

        BatchNotificationDispatcher batch =
                new BatchNotificationDispatcher(async);

        List<SmsNotification> notifications =
                List.of(
                        new SmsNotification("+1", "a"),
                        new SmsNotification("+2", "b")
                );

        List<NotificationResult> results =
                batch.sendBatchAsync(notifications).join();

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(NotificationResult::isSuccess));
    }
}
