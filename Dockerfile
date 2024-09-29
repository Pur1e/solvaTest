FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/transaction-service-0.1.2-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]