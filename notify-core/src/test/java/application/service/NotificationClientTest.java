package application.service;

import com.challenge.jorgebarreto.notifications.core.application.service.NotificationClient;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.AsyncNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.BatchNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.registry.NotificationChannelRegistry;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.domain.result.NotificationResult;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.publisher.KafkaNotificationEventPublisher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationClientTest {

    @Test
    void shouldSendSyncAsyncAndBatch() {
        NotificationChannelRegistry registry =
                new NotificationChannelRegistry();
        KafkaNotificationEventPublisher eventPublisher =
                new KafkaNotificationEventPublisher

        registry.register(
                SmsNotification.class,
                n -> NotificationResult.success()
        );

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(registry, eventPublisher);

        Executor executor = Executors.newFixedThreadPool(2);

        NotificationClient client =
                new NotificationClient(
                        dispatcher,
                        new AsyncNotificationDispatcher(dispatcher, executor),
                        new BatchNotificationDispatcher(
                                new AsyncNotificationDispatcher(dispatcher, executor)
                        )
                );

        assertTrue(
                client.send(new SmsNotification("+593", "hi")).isSuccess()
        );

        assertTrue(
                client.sendAsync(new SmsNotification("+593", "hi"))
                        .join()
                        .isSuccess()
        );

        assertEquals(
                2,
                client.sendBatchAsync(
                        List.of(
                                new SmsNotification("+1", "a"),
                                new SmsNotification("+2", "b")
                        )
                ).join().size()
        );
    }
}
