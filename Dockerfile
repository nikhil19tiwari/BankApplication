# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -e -DskipTests clean package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar onlineapp.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","onlineapp.jar"]