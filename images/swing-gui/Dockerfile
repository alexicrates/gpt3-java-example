FROM ubuntu:22.04

## Install OpenJDK-18
RUN apt-get update && apt upgrade -y
RUN apt-get install -y openjdk-18-jdk && apt-get clean
#RUN apt-get install add libxrender1:i386 libxtst6:i386 libxi6:i386
COPY . .

RUN ./mvnw clean
RUN ./mvnw --projects swing-gui verify -DskipTests

CMD ["java", "-jar", "/swing-gui/target/swing-gui-0.0.1-SNAPSHOT.jar"]