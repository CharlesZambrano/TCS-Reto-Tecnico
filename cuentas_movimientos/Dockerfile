FROM eclipse-temurin:21-jre-alpine
COPY build/libs/cuentas_movimientos-V1.jar /app/cuentas_movimientos-V1.jar
ENTRYPOINT [ "java", "-jar", "/app/cuentas_movimientos-V1.jar" ]
EXPOSE 8081
RUN apk --update --no-cache add curl
HEALTHCHECK --interval=1m --timeout=30s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1
LABEL version="0.1" \
    description="Cuentas-Movimientos microservice using Postgres\
    including Docker containers and health check test"