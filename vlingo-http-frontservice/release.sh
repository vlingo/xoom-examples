#!/usr/bin/env bash

docker build -t kaseoga/vlingo-examples-frontservice .
docker push kaseoga/vlingo-examples-frontservice
cf push vlingo-examples-frontservice --docker-image kaseoga/vlingo-examples-frontservice
