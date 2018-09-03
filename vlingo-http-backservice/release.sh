#!/usr/bin/env bash

docker build -t kaseoga/vlingo-examples-backservice .
docker push kaseoga/vlingo-examples-backservice
cf push vlingo-examples-backservice --docker-image kaseoga/vlingo-examples-backservice
