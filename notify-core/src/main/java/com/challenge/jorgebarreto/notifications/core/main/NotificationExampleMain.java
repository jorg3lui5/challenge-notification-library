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

public class NotificationExampleMain {

    public static void main(String[] args) {

        System.out.println("=== NOTIFICATION LIBRARY DEMO START ===");

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

        // Composite channel (multiple providers simulation)
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
        System.out.println("\n--- SUCCESS EMAIL ---");
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
        System.out.println("\n--- VALIDATION ERROR ---");
        client.send(
                new SmsNotification(
                        "123", // invalid phone
                        "Hello"
                )
        );

        // -------------------------
        // 3. PROVIDER FAILURE + RETRY
        // -------------------------
        System.out.println("\n--- PROVIDER FAILURE + RETRY ---");
        client.send(
                new SmsNotification(
                        "+593999999999",
                        "FAIL SMS"
                )
        );

        // -------------------------
        // 4. PUSH FAILURE
        // -------------------------
        System.out.println("\n--- PUSH FAILURE ---");
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
        System.out.println("\n--- ASYNC SEND ---");
        client.sendAsync(
                new EmailNotification(
                        "async@test.com",
                        "Async",
                        "Async message",
                        Map.of()
                )
        ).thenAccept(r ->
                System.out.println("Async result success=" + r.isSuccess())
        ).join();

        // -------------------------
        // 6. BATCH SEND
        // -------------------------
        System.out.println("\n--- BATCH SEND ---");
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
            System.out.println("Batch results:");
            results.forEach(r ->
                    System.out.println(" - success=" + r.isSuccess() +
                            " code=" + r.getErrorCode())
            );
        }).join();

        System.out.println("\n=== NOTIFICATION LIBRARY DEMO END ===");
    }
}

