FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

# Incluye Wrapper, configuración raíz y todos los módulos.
COPY . .

# Corrige los saltos de línea si trabajas desde Windows.
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew :shipping-service:bootstrap:bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring \
    /workspace/shipping-service/bootstrap/build/libs/app.jar \
    /app/app.jar

USER spring:spring

ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]