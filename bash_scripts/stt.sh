#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

whisper='python3 -m flask --app '$project_dir'/whisper-api-flask/app.py run --host=0.0.0.0';

cd "$project_dir" && $whisper