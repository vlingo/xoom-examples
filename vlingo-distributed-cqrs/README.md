# vlingo-distributed-cqrs

Command model is persisted by `Journal` into PostgreSQL. Query model is persisted by `StateStore` into PostgreSQL as well.

Commands for PostgreSQL docker image/container:
- start: `docker-compose up -d`
- stop: `docker-compose down`
- clean up docker volumes: `docker volume prune`

Start node1: `java -jar target/vlingo-distributed-cqrs.jar node1`

Start node2: `java -jar target/vlingo-distributed-cqrs.jar node2`

Start node3: `java -jar target/vlingo-distributed-cqrs.jar node3`

Only node1 handles HTTP requests!