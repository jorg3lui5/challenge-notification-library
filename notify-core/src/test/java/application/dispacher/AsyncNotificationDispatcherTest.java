package application.dispacher;

import com.challenge.jorgebarreto.notifications.core.application.dispacher.AsyncNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncNotificationDispatcherTest {

    @Test
    void shouldSendAsync() {
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
                        Executors.newSingleThreadExecutor()
                );

        CompletableFuture<NotificationResult> future =
                async.sendAsync(new SmsNotification("+593", "hi"));

        assertTrue(future.join().isSuccess());
    }
}
