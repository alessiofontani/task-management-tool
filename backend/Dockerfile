# Build stage
# Uses specified version of maven with specified java version to build the application
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download maven dependencies
RUN mvn dependency:go-offline -B

# Copy the source code to the working directory
COPY src ./src

# Build the app
RUN mvn clean package -DskipTests

# Runtime Stage
# Use the specified version for the runtime stage
FROM eclipse-temurin:21-jre as runtime

# Set the working directory
WORKDIR /app

# Copy the JAR from the first stage (builder)
COPY --from=builder /app/target/taskmanagementtool-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]