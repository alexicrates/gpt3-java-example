FROM amd64/ubuntu:22.04

## Install OpenJDK-18
RUN apt-get update && apt upgrade -y
RUN apt-get install -y openjdk-18-jdk && \
    apt-get clean;

RUN apt-get install -y alsa-base alsa-utils
COPY . .

RUN ./mvnw clean
RUN ./mvnw --projects speech-listener verify -DskipTests


CMD ["./mvnw", "--projects", "speech-listener", "spring-boot:run"]
