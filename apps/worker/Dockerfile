# ─── AVGC-XR Worker Service ───
# Non-root user (audit fix MEDIUM-008 / HIGH-022)
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S avgcxr && adduser -S avgcxr -G avgcxr
WORKDIR /app

COPY target/avgc-xr-worker-1.0.0.jar app.jar
RUN chown -R avgcxr:avgcxr /app

USER avgcxr
EXPOSE 8081
ENTRYPOINT ["java","-XX:+UseZGC","-XX:+ZGenerational","-Xmx512m","-Xms256m","-jar","app.jar"]
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD wget -qO- http://localhost:8081/actuator/health || exit 1
