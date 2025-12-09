FROM eclipse-temurin:23 AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and download dependencies
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Copy source and build
COPY src src
RUN ./mvnw -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
