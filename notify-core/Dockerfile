FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-cp", "target/notify-core-1.0.0.jar", "com.challenge.jorgebarreto.notifications.core.main.NotificationExampleMain"]
