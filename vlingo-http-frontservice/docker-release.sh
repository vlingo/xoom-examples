#!/usr/bin/env bash

IMAGE=kaseoga/vlingo-examples-frontservice

docker build -t $IMAGE .
docker push $IMAGE
