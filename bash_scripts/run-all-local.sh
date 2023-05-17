#!/bin/bash
# START APP ON LOCAL MACHINE

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

docker pull postgres;

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';
tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';
gpt='mvn --file '$project_dir'/gpt-api/pom.xml spring-boot:run'
listener='mvn --file '$project_dir'/speech-listener/pom.xml spring-boot:run'
db='bash '$project_dir'/bash_scripts/start-postgres.sh'
gui='mvn --file '$project_dir'/swing-gui/pom.xml spring-boot:run'

cd "$project_dir" && (trap 'kill 0' SIGINT; $db & $whisper & $tts & $gpt & $listener & $gui & wait)
