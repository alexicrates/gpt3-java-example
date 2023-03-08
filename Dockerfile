#FROM ubuntu
#
##Install Python, pip, torch
#RUN apt-get update && apt upgrade -y
#RUN apt-get install -y python3
#RUN apt install -y python3-pip
#
#COPY model.pt model.pt
#COPY python_scripts/tts.py ./python_scripts/tts.py
#COPY example.txt example.txt
#
#RUN pip3 install torch torchvision torchaudio --extra-index-url https://download.pytorch.org/whl/cpu
#RUN python3 ./python_scripts/tts.py example.txt
#
## Install OpenJDK-18
#RUN apt-get install -y openjdk-18-jdk && \
#    apt-get clean;
#
#RUN apt-get install -y alsa-base alsa-utils
#
#COPY target/gpt3-java-example-0.0.1-SNAPSHOT.jar gpt3-java-example-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/gpt3-java-example-0.0.1-SNAPSHOT.jar"]

FROM ubuntu:18.04
COPY python_scripts/detect.py detect.py
RUN apt-get update && apt upgrade -y
RUN apt-get install -y python3
RUN apt-get install -y python3-pyaudio
RUN apt-get install -y python3-setuptools
RUN apt-get install -y webrtcvad
RUN python3 detect.py
