 
FROM snmaddula/ubuntu-java 

RUN add-apt-repository ppa:andrei-pozolotin/maven3 -y && apt-get update && apt-get install maven3 -y

RUN git clone https://github.com/snmaddula/basic-ms-app.git
WORKDIR "/basic-ms-app"
RUN mvn clean spring-boot:run 

# CMD ["/bin/sh", "-c",  ". /etc/profile && java -Djava.security.egd=file:/dev/./urandom -jar target/app.jar"]
CMD echo "DONE!"


