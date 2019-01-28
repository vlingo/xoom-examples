#!/usr/bin/env bash

IMAGE=${1:-localhost:5000/vlingo-front:latest}

if [[ -z $IMAGE ]]; then
    echo "Image name should be defined: ./docker-release.sh <image_name>"
    exit 1;
fi

docker build -t $IMAGE .
docker push $IMAGE
