# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy POM and prefetch deps for faster CI
COPY pom.xml .
RUN mvn -q -DskipTests=true dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -q -DskipTests=true package

# ---------- Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
# Add a non-root user for better security (optional but recommended)
RUN useradd -ms /bin/bash appuser
COPY --from=build /app/target/*.jar app.jar

# Render (and many PaaS) inject PORT; Spring must bind to it
ENV PORT=8080
EXPOSE 8080

# Smaller heap helps free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -Xmx512m -XX:MaxRAMPercentage=75.0"

USER appuser
ENTRYPOINT ["java","-Dserver.port=${PORT}","-Dspring.profiles.active=prod","-jar","/app/app.jar"]
