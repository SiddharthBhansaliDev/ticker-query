FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src/ src/
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-XX:InitialRAMPercentage=75.0", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
