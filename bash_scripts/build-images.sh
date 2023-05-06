#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

docker build -f "$project_dir"/images/gpt-api/Dockerfile -t gpt-api "$project_dir"/gpt-api/target
docker build -f "$project_dir"/images/speech-listener-v2/Dockerfile -t speech-listener "$project_dir"
docker build -f "$project_dir"/images/swing-gui/Dockerfile -t swing-gui "$project_dir"
docker build -f "$project_dir"/images/whisper/Dockerfile -t whisper "$project_dir"/whisper-api-flask
docker build -f "$project_dir"/images/silero-tts/Dockerfile -t silero-tts "$project_dir"/silero_tts
