# Imagen base liviana con Java 21.
# Esta imagen solo ejecutará la aplicación ya compilada.
FROM eclipse-temurin:21-jre

# Carpeta interna de trabajo dentro del contenedor.
WORKDIR /app

# Copia el archivo JAR generado por Maven hacia el contenedor.
COPY target/api-pruebas-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto interno usado por Spring Boot.
EXPOSE 8080

# Comando que inicia la aplicación.
ENTRYPOINT ["java", "-jar", "app.jar"]