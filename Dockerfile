FROM eclipse-temurin:25-jdk-alpine
LABEL authors="brake"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]