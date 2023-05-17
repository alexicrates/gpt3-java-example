#!/bin/bash
#START APP INSIDE DOCKER CONTAINER

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';
tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';

listener='/mvnw --projects speech-listener spring-boot:run'
gpt='/mvnw --projects gpt-api spring-boot:run'
gui='/mvnw --projects swing-gui spring-boot:run'

(trap 'kill 0' SIGINT; $whisper & $tts & $gpt & $listener & $gui & wait)