#!/bin/bash
# START APP ON LOCAL MACHINE

project_dir=$(cd $(dirname "$0") && cd .. && pwd)
app_pids=()

start_app() {
    $1 & app_pids+=($!)
}

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';
tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';
gpt='mvn --file '$project_dir'/gpt-api/pom.xml spring-boot:run'
listener='mvn --file '$project_dir'/speech-listener/pom.xml spring-boot:run'
db='bash '$project_dir'/bash_scripts/start-postgres.sh'
gui='mvn --file '$project_dir'/swing-gui/pom.xml spring-boot:run'

start_app "$db"
start_app "$whisper"
start_app "$tts"
start_app "$gpt"
start_app "$listener"
start_app "$gui"

stop_apps() {
    echo "Stopping all apps"
    for pid in "${app_pids[@]}"; do
        if ps -p $pid > /dev/null; then
            echo "Stopping process with ID: $pid"
            kill $pid
        else
            echo "Process with ID $pid is not running"
        fi
    done
}

trap stop_apps SIGINT

wait

