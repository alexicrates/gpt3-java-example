FROM python:3.10-slim

WORKDIR /python-docker

RUN pip3 install flask
RUN apt-get update && apt-get install -y ffmpeg
RUN pip3 install torch torchaudio --index-url https://download.pytorch.org/whl/cpu
RUN pip install -U openai-whisper

COPY . .

EXPOSE 5000

CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]
