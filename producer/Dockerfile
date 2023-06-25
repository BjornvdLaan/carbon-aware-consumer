# Use a base image with Java 17
FROM amazoncorretto:17.0.7-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy the source code
COPY src src

# Run the Gradle build
RUN ./gradlew build

# Use a base image with Java 17
FROM amazoncorretto:17.0.7-alpine

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/producer-0.0.1-SNAPSHOT.jar app.jar

# Set the entry point command for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
