# Usa la imagen base de Java
FROM openjdk:11

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR construido en el directorio de trabajo
COPY target/CRUD.jar .

# Expone el puerto en el que se ejecuta tu aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "CRUD.jar"]

