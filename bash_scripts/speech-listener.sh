#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

cd "$project_dir" && mvn --projects speech-listener spring-boot:run

