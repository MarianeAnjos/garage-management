FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/garage-management-api-1.0-SNAPSHOT.jar app.jar
EXPOSE 3003
ENTRYPOINT ["java", "-jar", "app.jar"]
