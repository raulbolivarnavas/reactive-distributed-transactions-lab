# Validación de entrega

- Generación del Wrapper Gradle 9.0.0: correcta.
- Revisión de estructura, SQL y archivo ZIP: correcta.
- Pruebas y compilación: no completadas; Gradle no pudo resolver el plugin Spring Boot 4.1.1 desde los repositorios en este entorno. Se confirmó mediante HTTP que el POM existe en Maven Central.
- Un segundo intento superó la resolución del plugin usando la configuración de red, pero se detuvo porque no hay un compilador JDK instalado.
- Solo el runtime Java 17 está disponible en el entorno de creación; el proyecto está configurado para Java 21.
- Docker no está disponible aquí: ejecución HTTP/R2DBC pendiente de verificar en tu equipo.

Ejecuta `./gradlew test` (Linux/WSL) o `gradlew.bat test` (Windows) con JDK 21; después `docker compose up --build -d` y los escenarios del README.
