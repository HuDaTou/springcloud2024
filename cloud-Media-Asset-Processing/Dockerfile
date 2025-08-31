# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory in the builder stage
WORKDIR /workspace

# Copy the entire project context to the builder
COPY . .

# Build the specific module and its dependencies.
# This command leverages Maven's project aggregation capabilities.
RUN mvn clean package -pl cloud-Media-Asset-Processing -am -DskipTests

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the final image
WORKDIR /app

# Expose the port the application runs on
EXPOSE 8083

# Copy the built JAR from the builder stage to the final image
COPY --from=builder /workspace/cloud-Media-Asset-Processing/target/*.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
