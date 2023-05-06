#!/bin/bash
#START APP INSIDE DOCKER CONTAINER

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';
tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';
gpt=''$project_dir'/mvnw --file '$project_dir'/gpt-api/pom.xml spring-boot:run'
listener=''$project_dir'/mvnw --file '$project_dir'/speech-listener/pom.xml spring-boot:run'
gui=''$project_dir'/mvnw --file '$project_dir'/swing-gui/pom.xml spring-boot:run'

listener='/mvnw --projects speech-listener spring-boot:run'

(trap 'kill 0' SIGINT; $whisper & $tts & $gpt & $listener & $gui & wait)