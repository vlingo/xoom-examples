#!/usr/bin/env bash

IMAGE=kaseoga/vlingo-examples-backservice

docker build -t $IMAGE .
docker push $IMAGE
