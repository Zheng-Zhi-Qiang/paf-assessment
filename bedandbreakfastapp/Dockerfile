FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /app

COPY mvnw .
COPY pom.xml .
COPY src src
COPY .mvn .mvn

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/assessment-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV SPRING_DATASOURCE_URL=NOT_SET
ENV SPRING_DATASOURCE_USERNAME=NOT_SET
ENV SPRING_DATASOURCE_PASSWORD=NOT_SET
ENV SPRING_DATA_MONGODB_URI=NOT_SET

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar