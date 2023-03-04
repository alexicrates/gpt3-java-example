### 1. Get Linux with JDK
FROM openjdk:19-alpine

COPY --from=python / /

### 3. Get Python, PIP

ENV PYTHONUNBUFFERED=1
RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
RUN python3 -m ensurepip
RUN pip3 install --no-cache --upgrade pip setuptools

### Get Torch for the app
RUN pip3 install torch torchaudio --extra-index-url https://download.pytorch.org/whl/cpu

####
COPY python_scripts/tts.py tts.py
COPY model.pt model.pt
COPY example.txt example.txt

RUN  /usr/bin/python3 ./tts.py

#COPY target/gpt3-java-example-0.0.1-SNAPSHOT.jar gpt3-java-example-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/gpt3-java-example-0.0.1-SNAPSHOT.jar"]