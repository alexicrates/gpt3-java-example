#!/bin/bash

export JAVA_HOME=/home/alexicrates/.jdks/openjdk-19.0.2
export GPT_HOME='/home/alexicrates/Downloads/gpt3-java-example (3)/gpt3-java-example'

#mvn clean package --file=$GPT_HOME
docker build $GPT_HOME --tag=spring-app
docker compose up