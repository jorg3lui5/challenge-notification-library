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
- Arquitectura agnóstica a frameworks

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

---

## ⏱️ Envíos Asíncronos y en Lote

### Envío asíncrono

```java
CompletableFuture<NotificationResult> future =
    client.sendAsync(notification);
```

### Envío en lote

```java
CompletableFuture<List<NotificationResult>> results =
    client.sendBatchAsync(List.of(n1, n2, n3));
```

---

🔌 Proveedores Soportados (Simulados)

| Canal | Proveedor |
|-------|-----------|
| Email | Mailgun, SendGrid   |
| SMS   | Nexmo, Twilio     |
| Push  | Firebase, OneSignal  |

Nota: Los envíos son simulados mediante logs. No hay llamadas HTTP reales.

❗ Manejo de Errores

La librería distingue claramente entre:

- Errores de validación (VALIDATION)
- Errores del proveedor (PROVIDER)

Todos los envíos retornan un NotificationResult con:

- Estado (Boolean success)
- Código de error (errorCode)
- Mensaje (message)
- Timestamp (timestamp)
- Causa (cause)

🔁 Envío Asíncrono y en Lote

- Envío asíncrono con CompletableFuture
- Envío en batch con agregación de resultados
- Soporte para resultados parciales (CompositeNotificationResult)

🔐 Seguridad y Credenciales

- Las credenciales nunca se almacenan en archivos
- Se recomienda usar variables de entorno
- Interfaz CredentialsProvider permite abstraer el origen
- No loguear secretos  

---

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

---

## 🔌 Extensibilidad

Agregar un nuevo canal implica:

1. Crear una implementación de `Notification`
2. Crear un `NotificationChannel`
3. Implementar un `NotificationProviderPort`
4. Registrar el canal

---


🧪 Testing

La librería incluye tests unitarios básicos según lo requerido por el challenge.
Los envíos son simulados, no hay dependencias externas.

🐳 Docker

Incluye un Dockerfile para:
- Compilar la librería
- Ejecutar ejemplos sin instalar Java localmente

```bash
docker build -t notify-core .
docker run notify-core
```

📐 Arquitectura y Diseño

- Principios SOLID
- Patrones utilizados:
  - Strategy
  - Builder
  - Decorator
  - Facade
  - Composite

El diseño permite agregar nuevos canales o proveedores sin modificar el código existente.

🤖 Uso de IA

Este proyecto fue desarrollado con apoyo de ChatGPT (OpenAI) como asistente de diseño y revisión arquitectónica.
Las decisiones finales de arquitectura y código fueron tomadas manualmente por mi.

