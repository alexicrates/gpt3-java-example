#!/bin/bash
# START APP IN DOCKER CONTAINER

project_dir=$(cd $(dirname "$0") && cd .. && pwd)
app_pids=()

start_app() {
    $1 & app_pids+=($!)
}

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';
tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';
gpt='/mvnw --projects gpt-api spring-boot:run'
listener='/mvnw --projects speech-listener spring-boot:run'
gui='/mvnw --projects swing-gui spring-boot:run'

start_app "$whisper"
start_app "$tts"
start_app "$gpt"
start_app "$listener"
start_app "$gui"

stop_apps() {
    echo "Stopping all apps"
    for pid in "${app_pids[@]}"; do
        kill $pid
    done
}

trap stop_apps SIGINT

wait

