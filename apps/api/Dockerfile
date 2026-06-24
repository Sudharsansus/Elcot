# syntax=docker/dockerfile:1.6
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

# Install Maven
RUN apk add --no-cache maven bash

# Copy root pom
COPY pom.xml .

# Copy all module poms (for dependency resolution)
COPY packages/platform-core/pom.xml packages/platform-core/
COPY packages/platform-security/pom.xml packages/platform-security/
COPY packages/platform-events/pom.xml packages/platform-events/
COPY packages/platform-persistence/pom.xml packages/platform-persistence/
COPY packages/platform-observability/pom.xml packages/platform-observability/
COPY packages/platform-cms-client/pom.xml packages/platform-cms-client/
COPY packages/platform-notification-client/pom.xml packages/platform-notification-client/
COPY packages/platform-search-client/pom.xml packages/platform-search-client/
COPY apps/api/pom.xml apps/api/

# Pre-resolve dependencies (cached layer)
RUN mvn -B -ntp -pl apps/api -am dependency:go-offline

# Copy source code
COPY packages packages
COPY apps apps

# Build the API jar (skip tests — CI runs them)
RUN mvn -B -ntp -pl apps/api -am package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar
COPY --from=build /workspace/apps/api/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]