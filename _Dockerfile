FROM openjdk:8u181-jdk-alpine

VOLUME /tmp

ADD target/basic-ms-app-1.0.jar app.jar

RUN touch /app.jar

CMD ["/bin/sh", "-c",  ". /etc/profile && java -Djava.security.egd=file:/dev/./urandom -jar app.jar"]