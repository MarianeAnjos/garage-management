FROM eclipse-temurin:17-jdk-alpine

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação para o container
COPY target/garage-management-api-1.0-SNAPSHOT.jar app.jar

# Copiar o application.properties para garantir que a porta e config sejam carregadas
COPY src/main/resources/application.properties /app/application.properties

# Expor a porta correta
EXPOSE 3003

# Definir o entrypoint garantindo que o Spring Boot use a configuração correta
ENTRYPOINT ["java", "-Dspring.config.location=file:/app/application.properties", "-jar", "app.jar"]
