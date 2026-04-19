# Usamos una imagen oficial de Maven para construir la aplicacion de Spring Boot
FROM maven:3.9.9-eclipse-temurin-21 AS build
#Establecemos el directorio de trabajo
WORKDIR /app
#Copiar el pom.xml e instalar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline
#Copiar los recursos del codigo y construir la aplicacion
COPY src ./src
RUN mvn clean package -DskipTests
#Usar una imagen JDK official para correr la aplicacion
FROM eclipse-temurin:21-jdk
#Establecemos el directorio de trabajo
WORKDIR /app
#Copiar la el archivo JAR construido en la estapa de construccion
COPY --from=build /app/target/sb-ecom-0.0.1-SNAPSHOT.jar .
#Exponer el puerto 8080
EXPOSE 8080
#Specificar el comando para correr la aplicacion
ENTRYPOINT ["java", "-jar", "/app/sb-ecom-0.0.1-SNAPSHOT.jar"]













