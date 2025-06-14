# Etapa 1: build usando Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem mínima para rodar
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/garage-management-api-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
