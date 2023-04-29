#!/bin/bash

project_dir=$(cd $(dirname "$0") && cd .. && pwd)
docker compose --file "$project_dir"/docker-compose-only-postgres.yaml up --detach