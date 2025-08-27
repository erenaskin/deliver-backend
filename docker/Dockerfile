# Multi-stage build for Spring Boot application
FROM openjdk:17-jdk-slim as builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn ./.mvn

# Build the application
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
