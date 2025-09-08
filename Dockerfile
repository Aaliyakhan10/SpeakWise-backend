# Stage 1: Build the application
FROM gradle:8.8-jdk21 AS build
WORKDIR /app

# Copy gradle wrapper and gradle config files first for better caching
COPY --chown=gradle:gradle gradlew gradlew.bat /app/
COPY --chown=gradle:gradle gradle /app/gradle
COPY --chown=gradle:gradle build.gradle settings.gradle /app/
RUN chmod +x ./gradlew
# Download dependencies before copying source for caching
RUN ./gradlew --version

# Copy source code
COPY --chown=gradle:gradle src /app/src

# Build the application
RUN ./gradlew clean build --no-daemon -x test --stacktrace --info

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/SpeakWise-0.0.1-SNAPSHOT.jar /app/application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]