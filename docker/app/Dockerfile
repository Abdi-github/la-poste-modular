# ──────────────────────────────────────────────────────────
# La Poste Modular — Multi-Stage Production Dockerfile
# ──────────────────────────────────────────────────────────

# ════════════════════════════════════════════════════════════
# STAGE 1 — Builder: Compile and package with Maven
# ════════════════════════════════════════════════════════════
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B -q
COPY src/ src/
RUN ./mvnw package -DskipTests -B -q && mv target/*.jar target/app.jar

# ════════════════════════════════════════════════════════════
# STAGE 2 — Layers: Extract Spring Boot layered JAR
# ════════════════════════════════════════════════════════════
FROM eclipse-temurin:21-jdk AS layers
WORKDIR /extract
COPY --from=builder /build/target/app.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# ════════════════════════════════════════════════════════════
# STAGE 3 — Runtime: Minimal JRE image
# ════════════════════════════════════════════════════════════
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL maintainer="La Poste Modular <dev@swiftapp.ch>"
LABEL description="La Poste Modular — Swiss Postal Service Platform"

RUN apk add --no-cache curl tzdata \
    && cp /usr/share/zoneinfo/Europe/Zurich /etc/localtime \
    && echo "Europe/Zurich" > /etc/timezone \
    && apk del tzdata

RUN addgroup -g 1000 -S appgroup \
    && adduser -u 1000 -S appuser -G appgroup

WORKDIR /app

COPY --from=layers /extract/extracted/dependencies/ ./
COPY --from=layers /extract/extracted/spring-boot-loader/ ./
COPY --from=layers /extract/extracted/snapshot-dependencies/ ./
COPY --from=layers /extract/extracted/application/ ./

RUN chown -R appuser:appgroup /app
USER appuser:appgroup

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -Djava.security.egd=file:/dev/./urandom \
    -Duser.timezone=Europe/Zurich"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -sf http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher"]

