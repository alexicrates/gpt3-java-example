#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

tts='python3 -m flask --app '$project_dir'/silero_tts/app.py run --host=0.0.0.0 --port 5001';

cd "$project_dir" && $tts