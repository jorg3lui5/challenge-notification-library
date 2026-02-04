FROM maven:3.9.9-eclipse-temurin-21-alpine

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package dependency:copy-dependencies -DskipTests

CMD ["java", "-cp", "target/notify-core-1.0.0.jar:target/dependency/*", "com.challenge.jorgebarreto.notifications.core.main.NotificationExampleMain"]