# Usar una imagen base de Java 11
FROM openjdk:21-slim

# Copiar el archivo jar de la aplicación al contenedor
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Exponer el puerto 8080 para permitir la comunicación con la aplicación
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]