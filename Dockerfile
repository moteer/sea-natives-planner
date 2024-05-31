# Use a base image with OpenJDK 11
FROM openjdk:latest

# Set the maintainer label
LABEL maintainer="erik.hoelzel@posteo.de"

# Add a volume pointing to /tmp
VOLUME /tmp

ARG JAR_FILE_NAME=sea-natives-app-0.0.1-SNAPSHOT.jar

# Define a build argument to specify the JAR file name with a default value
ARG JAR_FILE=target/${JAR_FILE_NAME}

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
