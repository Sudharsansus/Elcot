# ─── Stage 1: Download dependencies ───
FROM eclipse-temurin:21-jdk-alpine AS deps
WORKDIR /build
COPY pom.xml .
COPY packages/platform-core/pom.xml packages/platform-core/pom.xml
COPY packages/platform-security/pom.xml packages/platform-security/pom.xml
COPY packages/platform-events/pom.xml packages/platform-events/pom.xml
COPY packages/platform-persistence/pom.xml packages/platform-persistence/pom.xml
COPY packages/platform-observability/pom.xml packages/platform-observability/pom.xml
COPY packages/platform-cms-client/pom.xml packages/platform-cms-client/pom.xml
COPY packages/platform-notification-client/pom.xml packages/platform-notification-client/pom.xml
COPY packages/platform-search-client/pom.xml packages/platform-search-client/pom.xml

RUN mkdir -p packages/platform-core/src packages/platform-security/src \
             packages/platform-events/src packages/platform-persistence/src \
             packages/platform-observability/src packages/platform-cms-client/src \
             packages/platform-notification-client/src packages/platform-search-client/src

# Create dummy sources so Maven can resolve the parent POM and all modules
RUN for pkg in platform-core platform-security platform-events platform-persistence \
              platform-observability platform-cms-client platform-notification-client \
              platform-search-client; do \
    echo "package in.elcot.avgcxr.$pkg;" > packages/$pkg/src/main/java/dummy.java; \
    echo "package in.elcot.avgcxr.$pkg;" > packages/$pkg/src/test/java/dummy.java; \
  done

RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B || true

# ─── Stage 2: Build application ───
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build
COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml .
COPY --from=deps /build/packages packages
COPY apps/api/pom.xml apps/api/pom.xml
COPY apps/api/src apps/api/src

RUN mvn package -DskipTests -B --offline

# ─── Stage 3: Runtime ───
FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="ELCOT AVGC-XR Team" \
      description="Tamil Nadu AVGC-XR Portal API Service"

RUN addgroup -S avgcxr && adduser -S avgcxr -G avgcxr
WORKDIR /app

COPY --from=build /build/apps/api/target/api.jar app.jar
RUN chown -R avgcxr:avgcxr /app

USER avgcxr
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "--enable-preview", "-XX:+UseZGC", "-XX:+ZGenerational", \
           "-Xms256m", "-Xmx768m", \
           "-Djava.security.egd=file:/dev/./urandom", \
           "-jar", "app.jar"]
