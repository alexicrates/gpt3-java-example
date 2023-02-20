FROM openjdk
COPY target/gpt3-java-example-0.0.1-SNAPSHOT.jar gpt3-java-example-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/gpt3-java-example-0.0.1-SNAPSHOT.jar"]
