#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)
docker pull postgres;
docker compose --file "$project_dir"/postgres.yaml up --detach