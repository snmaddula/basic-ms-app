FROM openjdk:8u181-jdk-alpine
ADD target/portal-api-0.0.1-SNAPSHOT.jar app.jar
RUN touch /app.jar
CMD ["/bin/sh", "-c",  ". /etc/profile && java -Djava.security.egd=file:/dev/./urandom -jar app.jar"]