FROM eclipse-temurin:17-alpine

LABEL maintainer="dev@ljos.app"

# The application's jar file
ARG JAR_FILE=build/libs/budzilla.jar

# Add the application's jar to the container
ADD ${JAR_FILE} budzilla.jar

# Make sure the budzilla.health file has been written in the last five minutes
HEALTHCHECK CMD /usr/bin/find /tmp/budzilla.health -mmin -5 -type f | /bin/grep -q "/tmp/budzilla.health"

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/budzilla.jar"]
