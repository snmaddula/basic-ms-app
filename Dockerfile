 
FROM openjdk:8u181-jdk-alpine

VOLUME /tmp

RUN git clone https://github.com/snmaddula/basic-ms-app.git
WORKDIR "/basic-ms-app"
RUN runapp

# CMD ["/bin/sh", "-c",  ". /etc/profile && java -Djava.security.egd=file:/dev/./urandom -jar target/app.jar"]
CMD echo "DONE!"


