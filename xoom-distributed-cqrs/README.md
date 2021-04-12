# xoom-distributed-cqrs example

Command model is persisted by `Journal` into PostgreSQL. Query model is persisted by `StateStore` into PostgreSQL as well.

---

## Building and running locally

Prerequisites: Change 'hosts' to localhost in `xoom-cluster.properties` and `xoom-cars.properties`.

Commands for PostgreSQL docker image/container:
- start: `docker-compose up -d`
- stop: `docker-compose down`
- clean up docker volumes: `docker volume prune`

Start node1: `java -jar target/xoom-distributed-cqrs.jar node1`

Start node2: `java -jar target/xoom-distributed-cqrs.jar node2`

Start node3: `java -jar target/xoom-distributed-cqrs.jar node3`

Only node1 handles HTTP requests!

---

## Building and running into Kubernetes cluster

Prerequisites: Docker Desktop (https://www.docker.com/products/docker-desktop). All k8s config files are located in `src/main/k8s` folder.

- Build docker image: `docker build -t vlingo/xoom-distributed-cqrs:1.0 .`
- Kubernetes namespace: `kubectl [create | delete] namespace xoom-space`
- PostgreSQL Service and Deployment: `kubectl [create | delete] -f postgresql.yml`
- Application (3x nodes) Service and Deployment: `kubectl -f [create | delete] nodes.yml`

---

## REST API requests:

Car command:
```
curl --request POST 'http://localhost:30080/api/cars' \
  --header 'Content-Type: application/json' \  
  --data-raw '{
    "type":"Audi",  
    "model":"A4",
    "registrationNumber":"AB-01-ABC"
  }'
```

Cars query:
```
curl --request GET 'http://localhost:30080/api/cars'
```

---

Each node has exposed a debugging port:
- node1: 30571
- node2: 30572
- node3: 30573

Observe node logs: choose a docker container (`docker ps`) and then `docker logs <container_id>`