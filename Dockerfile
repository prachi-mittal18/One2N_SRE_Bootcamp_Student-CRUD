
# syntax=docker/dockerfile:1

# ---- Stage 1: Build ----
# Alpine-based JDK image keeps the build stage itself small, and using the
# full JDK here (not JRE) is required since we need javac to compile.
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy only the dependency manifest + wrapper first so Docker can cache the
# dependency download layer independently of source code changes.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Now bring in the source and build the jar. Tests are skipped here since
# they should run in CI prior to the image build, not gate it.
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ---- Stage 2: Runtime ----
# JRE-only, Alpine-based image: no compiler, no Maven, no build cache,
# no source code. This is what actually ships.
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Run as a non-root user rather than the container default (root).
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Only the built artifact crosses the stage boundary.
COPY --from=build /app/target/*.jar app.jar

# Matches server.port in application.properties. Purely documentation —
# the actual port publishing happens via `docker run -p`.
EXPOSE 8085

# DB_URL / DB_USERNAME / DB_PASSWORD are read from application.properties
# with in-memory H2 defaults, so no env vars are required to run, but can
# be overridden at `docker run` time with -e or --env-file.
ENTRYPOINT ["java", "-jar", "app.jar"]