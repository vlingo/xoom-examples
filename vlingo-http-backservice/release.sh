#!/usr/bin/env bash

docker build -t kaseoga/vlingo-examples-backservice .
docker push kaseoga/vlingo-examples-backservice
cf push vlingo-examples-backservice --docker-image kaseoga/vlingo-examples-backservice
cf map-route vlingo-examples-backservice apps.internal --hostname vlingo-examples-backservice
cf add-network-policy vlingo-examples-frontservice --destination-app vlingo-examples-backservice --port 8082 --protocol tcp
cf delete-route cfapps.io --hostname vlingo-examples-backservice
