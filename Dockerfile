FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080
COPY ./target/metadata-0.0.1-SNAPSHOT.jar /app/metadata.jar
CMD ["java", "-jar", "app/metadata.jar"]