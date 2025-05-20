# 빌드 스테이지
FROM gradle:8.6.0-jdk21 AS builder

WORKDIR /build

# 의존성 캐시 최적화
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN gradle dependencies --no-daemon || true

# 소스 복사 및 빌드
COPY src src
RUN gradle build --no-daemon -x test

# 런타임 스테이지
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# curl 설치 (헬스체크용)
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# JAR 복사
COPY --from=builder /build/build/libs/mixingbowl-0.0.1-SNAPSHOT.jar /app/app.jar

# 비루트 사용자 생성
RUN adduser --disabled-password --gecos '' appuser
USER appuser

# 포트 및 환경변수 명확화
ENV SERVER_PORT=8080

# 엔트리포인트 및 헬스체크
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]