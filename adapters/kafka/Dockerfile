FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /workspace
COPY . .
RUN gradle :adapters:kafka:bootJar --no-daemon -x test


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/adapters/kafka/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]