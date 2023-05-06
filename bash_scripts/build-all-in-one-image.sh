#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

docker build -f "$project_dir"/images/all-in-one/Dockerfile -t voice-gpt-app "$project_dir"