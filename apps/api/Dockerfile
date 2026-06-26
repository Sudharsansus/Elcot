# syntax=docker/dockerfile:1.6

# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /workspace
RUN apk add --no-cache maven bash
COPY pom.xml .
COPY checkstyle.xml .
COPY packages packages
COPY apps apps
RUN mvn -B -ntp -pl apps/api -am package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S avgcxr 2>/dev/null; adduser -S avgcxr -G avgcxr 2>/dev/null; true
USER avgcxr
COPY --from=build /workspace/apps/api/target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]