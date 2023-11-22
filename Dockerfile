FROM eclipse-temurin:17-jdk-alpine
ENV appName="real-estate-db-access"
EXPOSE 8080
LABEL name=$appName
COPY target/*.jar $appName.jar
ENTRYPOINT java -jar ./$appName.jar