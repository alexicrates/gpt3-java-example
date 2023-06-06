#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)

gpt='mvn --file '$project_dir'/gpt-api/pom.xml spring-boot:run'

cd "$project_dir" && $gpt