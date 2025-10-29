# ---------- Etapa 1: Build ----------
FROM gradle:8.9-jdk21-alpine AS builder
WORKDIR /workspace

# Copiar archivos necesarios de Gradle
COPY gradle gradle
COPY gradlew .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src

# Compilar y empaquetar la aplicación
RUN ./gradlew --no-daemon clean bootJar -x test

# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el artefacto generado desde la etapa anterior
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
