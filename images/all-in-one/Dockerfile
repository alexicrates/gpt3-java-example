FROM ubuntu:22.04

#Install python and related stuff
RUN apt-get update && apt upgrade -y
RUN apt-get install -y python3
RUN apt install -y python3-pip

RUN pip3 install flask
RUN pip3 install playsound
RUN pip3 install torch torchaudio --index-url https://download.pytorch.org/whl/cpu
RUN pip3 install numpy
RUN pip3 install IPython

RUN apt-get install -y ffmpeg
RUN pip install -U openai-whisper

## Install OpenJDK-18
RUN apt-get install -y openjdk-18-jdk
RUN apt-get install -y alsa-base alsa-utils
RUN apt-get clean

COPY . .

RUN /mvnw --projects gpt-logs-spring-boot-starter clean install
RUN /mvnw clean verify -DskipTests
CMD ["bash", "./bash_scripts/run-all.sh"]
