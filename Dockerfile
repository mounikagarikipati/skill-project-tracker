# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests=true dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests=true package

# ---------- Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -ms /bin/bash appuser
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -Xmx512m -XX:MaxRAMPercentage=75.0"

USER appuser
ENTRYPOINT ["java","-Dserver.port=${PORT}","-Dspring.profiles.active=prod","-jar","/app/app.jar"]
