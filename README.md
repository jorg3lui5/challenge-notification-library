# Notifications Core Library

Esta es una librería de notificaciones en Java, agnóstica a frameworks, diseñada para unificar el envío de notificaciones a través de múltiples canales como Email, SMS y Push Notification.

El objetivo es permitir cambiar proveedores o canales sin modificar el código cliente, priorizando una arquitectura limpia, extensible y basada en principios SOLID.

---

## 🚀 Características

- Interfaz unificada para todos los canales
- Canales soportados:
  - Email
  - SMS
  - Push Notification
- Configuración 100% por código Java
- Soporte para múltiples proveedores por canal
- Manejo claro de errores
- Envío síncrono, asíncrono y en lote
- Sistema de reintentos configurable
- Validaciones extensibles
- Sin dependencias de frameworks

---

## 📦 Instalación

### Maven

```xml
<dependency>
  <groupId>com.challenge.jorgebarreto</groupId>
  <artifactId>notify-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

⚡ Quick Start
```java
EmailChannel emailChannel =
    new EmailChannel(
        new MailgunEmailProvider("MAILGUN_API_KEY"),
        List.of()
    );

NotificationDispatcher dispatcher =
    NotificationDispatcherBuilder.builder()
        .withRetry(2)
        .registerChannel(EmailNotification.class, emailChannel)
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

client.send(
    new EmailNotification(
        "test@test.com",
        "Hello",
        "This is a test message",
        Map.of()
    )
);
```


---

## 📡 Canales soportados

### ✉️ Email

```java
EmailChannel emailChannel =
    new EmailChannel(
        new MailgunEmailProvider("MAILGUN_TOKEN"),
        List.of()
    );
```

**Validaciones incluidas:**
- Email válido  
- Recipient y message obligatorios  

---

### 📱 SMS

```java
SmsNotificationChannel smsChannel =
    new SmsNotificationChannel(
        new NexmoSmsProvider("API_KEY", "API_SECRET"),
        List.of()
    );
```

**Validaciones incluidas:**
- Número de teléfono válido (E.164)  
- Recipient y message obligatorios  

---

### 🔔 Push Notification

```java
PushNotificationChannel pushChannel =
    new PushNotificationChannel(
        new FirebasePushProvider("FIREBASE_CREDENTIALS"),
        List.of()
    );
```

**Validaciones incluidas:**
- Token de push válido  
- Recipient y message obligatorios  

---

⚙️ Configuración

Toda la configuración se realiza mediante código Java:

- Registro de canales mediante `NotificationDispatcherBuilder`
- Inyección de proveedores por constructor
- Soporte para múltiples proveedores por canal
- Configuración de reintentos sin modificar el core

Ejemplo con reintentos:

```java
NotificationDispatcher dispatcher =
    NotificationDispatcherBuilder.builder()
        .withRetry(3)
        .registerChannel(EmailNotification.class, emailChannel)
        .registerChannel(SmsNotification.class, smsChannel)
        .build();
```

---

## 🔁 Reintentos

La librería incluye un sistema de reintentos basado en el patrón **Decorator**.

- Reintenta solo errores de envío  
- No reintenta errores de validación ni configuración  
- Totalmente configurable

### ¿Cómo funcionan los reintentos?

- Los reintentos se aplican **por canal**
- Cada notificación se procesa **de forma independiente**
- El retry envuelve al canal mediante el patrón **Decorator**

```java
NotificationDispatcher dispatcher =
    NotificationDispatcherBuilder.builder()
        .withRetry(2)
        .registerChannel(EmailNotification.class, emailChannel)
        .build();
```

### Semántica exacta

- `withRetry(0)` → 1 intento total
- `withRetry(2)` → 1 intento inicial + 2 reintentos (3 intentos en total)

### ¿Cuándo se reintenta?

- ✅ Errores de proveedor
- ❌ Errores de validación
- ❌ Errores de configuración

### ¿Aplica a async y batch?

Sí:
- Envío síncrono: retry inmediato
- Envío asíncrono: retry dentro del `CompletableFuture`
- Batch: cada notificación aplica retry de forma individual

---
---

## ⏱️ Envíos Asíncronos y en Lote

## ⏱️ Envío asíncrono

```java
CompletableFuture<NotificationResult> future =
    client.sendAsync(notification);
```

- No bloquea el hilo llamador
- Usa `Executor` configurable
- Maneja excepciones técnicas automáticamente

---

## 📦 Envío en batch

```java
CompletableFuture<List<NotificationResult>> results =
    client.sendBatchAsync(List.of(n1, n2, n3));
```

Características:
- Cada notificación se procesa de forma independiente
- Soporta resultados parciales
- Los retries aplican por notificación

---
## 🔗 Múltiples canales y proveedores

### Registrar múltiples canales

```java
dispatcherBuilder
    .registerChannel(EmailNotification.class, emailChannel)
    .registerChannel(SmsNotification.class, smsChannel)
    .registerChannel(PushNotification.class, pushChannel);
```

### Múltiples proveedores (Composite)

```java
CompositeNotificationChannel<EmailNotification> compositeEmail =
    new CompositeNotificationChannel<>(
        List.of(mailgunChannel, sendGridChannel)
    );
```

- Todos los proveedores se ejecutan
- Retorna éxito total o fallo parcial

---

## 📝 Templates de Mensajes

La librería soporta templates simples para personalizar mensajes dinámicamente,
especialmente útiles en notificaciones por Email.

Los templates utilizan placeholders con el formato:

{{variable}}

Ejemplo:

```java
client.send(
    new EmailNotification(
        "user@test.com",
        "Pedido enviado",
        "Hola {{name}}, tu pedido {{orderId}} fue enviado",
        Map.of(
            "name", "Jorge",
            "orderId", "12345"
        )
    )
);
```
Antes del envío, el motor de templates reemplaza automáticamente las variables
usando el mapa de atributos.

Actualmente se utiliza un motor simple y extensible,
pensado para evolucionar hacia soluciones más avanzadas sin afectar el core.

---

## 📣 Publicación de Eventos (Pub/Sub)

La librería incluye un sistema de publicación de eventos para notificar el
resultado de cada envío, siguiendo el patrón **Publisher / Subscriber**.

Cada envío genera un `NotificationEvent` que contiene:

- Tipo de notificación
- Destinatario
- Resultado (success / failure)
- Código de error
- Timestamp

### Publishers disponibles

- `InMemoryNotificationEventPublisher`  
  Para pruebas o listeners locales
- `KafkaNotificationEventPublisher`  
  Simulación de publicación de eventos a Kafka
- `NoOpNotificationEventPublisher`  
  Ignora los eventos (default)

### Ejemplo con listener

```java
NotificationDispatcher dispatcher =
    NotificationDispatcherBuilder.builder()
        .withEventPublisher(new InMemoryNotificationEventPublisher())
        .addListener(new LoggingNotificationListener())
        .registerChannel(EmailNotification.class, emailChannel)
        .build();
```
Este diseño permite integrar fácilmente métricas, auditoría,
logging o sistemas externos sin acoplar el core de la librería.


---

## 🧩 Extensibilidad

### Crear un nuevo canal

Ejemplo: WhatsApp

#### 1️⃣ Crear la notificación

```java
public record WhatsAppNotification(
    String recipient,
    String message
) implements Notification {}
```

#### 2️⃣ Crear el proveedor

```java
public class WhatsAppProvider
        implements NotificationProviderPort<WhatsAppNotification> {

    @Override
    public ProviderResult send(WhatsAppNotification notification) {
        return ProviderResult.ok();
    }
}
```

#### 3️⃣ Crear el canal

```java
public class WhatsAppChannel
        implements NotificationChannel<WhatsAppNotification> {

    private final NotificationProviderPort<WhatsAppNotification> provider;

    @Override
    public NotificationResult send(WhatsAppNotification notification) {
        ProviderResult result = provider.send(notification);

        return result.success()
            ? NotificationResult.success()
            : NotificationResult.failure(
                result.errorCode(),
                result.message(),
                result.cause()
            );
    }
}
```

#### 4️⃣ Registrar el canal

```java
dispatcherBuilder.registerChannel(
    WhatsAppNotification.class,
    new WhatsAppChannel(new WhatsAppProvider())
);
```

---

## 🔌 Crear un proveedor personalizado

Solo debes implementar:

```java
NotificationProviderPort<T>
```

Ejemplo:

```java
public class CustomEmailProvider
        implements NotificationProviderPort<EmailNotification> {

    @Override
    public ProviderResult send(EmailNotification notification) {
        return ProviderResult.ok();
    }
}
```

---

🔌 Proveedores Soportados (Simulados)

| Canal | Proveedor |
|-------|-----------|
| Email | Mailgun, SendGrid   |
| SMS   | Nexmo, Twilio     |
| Push  | Firebase, OneSignal  |

Nota: Los envíos son simulados mediante logs. No hay llamadas HTTP reales.

## ❗ Manejo de errores

Tipos de errores:

- VALIDATION
- PROVIDER
- CONFIGURATION

Todos los envíos retornan un NotificationResult con:

- Estado (Boolean success)
- Código de error (errorCode)
- Mensaje (message)
- Timestamp (timestamp)
- Causa (cause)


## 🔐 Seguridad y Credenciales

- Las credenciales nunca se almacenan en archivos
- Se recomienda usar variables de entorno
- Interfaz CredentialsProvider permite abstraer el origen
- No loguear secretos  



## 📚 API Reference

| Clase | Descripción |
|------|------------|
| Notification | Interfaz base de notificación |
| NotificationChannel | Canal de envío |
| NotificationDispatcher | Orquestador principal |
| NotificationDispatcherBuilder | Configuración |
| NotificationClient | API pública |
| NotificationResult | Resultado |
| RetryNotificationChannel | Reintentos |
| AsyncNotificationDispatcher | Async |
| BatchNotificationDispatcher | Batch |
| NotificationProviderPort | Proveedor |


## 🧪 Testing

La librería incluye tests unitarios básicos según lo requerido por el challenge.
Los envíos son simulados, no hay dependencias externas.


## 🐳 Docker

Incluye un Dockerfile para:
- Compilar la librería
- Ejecutar ejemplos sin instalar Java localmente

```bash
docker build -t notify-core .
docker run --rm notify-core
```
## 📐 Arquitectura y Diseño

- Principios SOLID
- Arquitectura Hexagonal
- Patrones utilizados:
  - Strategy
  - Builder
  - Decorator
  - Facade
  - Composite

El diseño permite agregar nuevos canales o proveedores sin modificar el código existente.

### Flujo General

1. El cliente envía una `Notification`
2. El `NotificationDispatcher` resuelve el canal
3. El canal valida y delega al proveedor
4. Se aplica retry si corresponde
5. Se publica el evento de resultado

Este flujo permite mantener responsabilidades claras y bajo acoplamiento.

---

🤖 Uso de IA

Este proyecto fue desarrollado con apoyo de ChatGPT (OpenAI) como asistente de diseño y revisión arquitectónica.
Las decisiones finales de arquitectura y código fueron tomadas manualmente por mi.

