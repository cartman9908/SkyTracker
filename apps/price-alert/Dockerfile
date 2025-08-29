FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

# 전체 프로젝트 복사
COPY ../.. .

# BootJar 빌드
RUN gradle :apps:price-alert:bootJar --no-daemon -x test

# 2단계: 실행 이미지
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/apps/price-alert/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]