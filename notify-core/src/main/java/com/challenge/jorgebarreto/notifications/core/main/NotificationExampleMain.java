package com.challenge.jorgebarreto.notifications.core.main;

import com.challenge.jorgebarreto.notifications.core.application.composite.CompositeNotificationChannel;
import com.challenge.jorgebarreto.notifications.core.application.service.NotificationClient;
import com.challenge.jorgebarreto.notifications.core.application.builder.NotificationDispatcherBuilder;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.AsyncNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.BatchNotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.application.dispacher.NotificationDispatcher;
import com.challenge.jorgebarreto.notifications.core.domain.model.EmailNotification;
import com.challenge.jorgebarreto.notifications.core.domain.model.PushNotification;
import com.challenge.jorgebarreto.notifications.core.domain.model.SmsNotification;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel.PushChannel;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel.SmsChannel;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.email.MailgunEmailProvider;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.channel.EmailChannel;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.push.FirebasePushProvider;
import com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.provider.sms.NexmoSmsProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationExampleMain {

    public static void main(String[] args) {

        log.info("=== NOTIFICATION LIBRARY DEMO START ===");

        // -------------------------
        // Providers
        // -------------------------
        MailgunEmailProvider mailgun =
                new MailgunEmailProvider("fake-token");

        NexmoSmsProvider nexmo =
                new NexmoSmsProvider("api-key", "secret");

        FirebasePushProvider firebase =
                new FirebasePushProvider("credentials");

        // -------------------------
        // Channels
        // -------------------------
        EmailChannel emailChannel =
                new EmailChannel(mailgun, List.of());

        SmsChannel smsChannel =
                new SmsChannel(nexmo, List.of());

        PushChannel pushChannel =
                new PushChannel(firebase, List.of());

        CompositeNotificationChannel<EmailNotification> compositeEmail =
                new CompositeNotificationChannel<>(
                        List.of(emailChannel)
                );

        // -------------------------
        // Dispatcher
        // -------------------------
        NotificationDispatcher dispatcher =
                NotificationDispatcherBuilder.builder()
                        .withRetry(2)
                        .registerChannel(EmailNotification.class, compositeEmail)
                        .registerChannel(SmsNotification.class, smsChannel)
                        .registerChannel(PushNotification.class, pushChannel)
                        .build();

        NotificationClient client =
                new NotificationClient(
                        dispatcher,
                        new AsyncNotificationDispatcher(
                                dispatcher,
                                Executors.newFixedThreadPool(2)
                        ),
                        new BatchNotificationDispatcher(
                                new AsyncNotificationDispatcher(
                                        dispatcher,
                                        Executors.newFixedThreadPool(2)
                                )
                        )
                );

        // -------------------------
        // 1. SUCCESS CASE
        // -------------------------
        log.info("--- SUCCESS EMAIL ---");
        client.send(
                new EmailNotification(
                        "test@test.com",
                        "Hello",
                        "Email OK",
                        Map.of()
                )
        );

        // -------------------------
        // 2. VALIDATION ERROR
        // -------------------------
        log.info("--- VALIDATION ERROR ---");
        client.send(
                new SmsNotification(
                        "123",
                        "Hello"
                )
        );

        // -------------------------
        // 3. PROVIDER FAILURE + RETRY
        // -------------------------
        log.info("--- PROVIDER FAILURE + RETRY ---");
        client.send(
                new SmsNotification(
                        "+593999999999",
                        "FAIL SMS"
                )
        );

        // -------------------------
        // 4. PUSH FAILURE
        // -------------------------
        log.info("--- PUSH FAILURE ---");
        client.send(
                new PushNotification(
                        "valid-push-token-12345",
                        "Alert",
                        "FAIL PUSH",
                        Map.of()
                )
        );

        // -------------------------
        // 5. ASYNC SEND
        // -------------------------
        log.info("--- ASYNC SEND ---");
        client.sendAsync(
                new EmailNotification(
                        "async@test.com",
                        "Async",
                        "Async message",
                        Map.of()
                )
        ).thenAccept(result ->
                log.info("Async result success={}", result.isSuccess())
        ).join();

        // -------------------------
        // 6. BATCH SEND
        // -------------------------
        log.info("--- BATCH SEND ---");
        client.sendBatchAsync(
                List.of(
                        new EmailNotification(
                                "batch1@test.com",
                                "Batch",
                                "OK",
                                Map.of()
                        ),
                        new SmsNotification(
                                "+593888888888",
                                "FAIL SMS"
                        ),
                        new PushNotification(
                                "token-123456789",
                                "Batch",
                                "OK",
                                Map.of()
                        )
                )
        ).thenAccept(results -> {
            log.info("Batch results:");
            results.forEach(r ->
                    log.info(" - success={} code={}",
                            r.isSuccess(),
                            r.getErrorCode())
            );
        }).join();

        log.info("=== NOTIFICATION LIBRARY DEMO END ===");
    }
}

