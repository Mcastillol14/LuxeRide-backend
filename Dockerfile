# Utiliza una imagen base con Java 17
FROM openjdk:17-jdk-slim

# Copia el archivo JAR desde el directorio target
COPY ./target/taxistfg-0.0.1-SNAPSHOT.jar  app.jar


# Ejecuta el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
