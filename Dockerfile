FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/ifba-0.0.1-SNAPSHOT.jar ifba-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "ifba-0.0.1-SNAPSHOT.jar"]