# xoom-distributed-spaces example

Each change on distributed spaces will be replicated across all nodes.

---

Build example: `mvn clean package`

Run node1: `java -jar target/xoom-distributed-spaces.jar -Dnode=1:node1:true:localhost:17371:17372 -Dport=18081`

Run node2: `java -jar target/xoom-distributed-spaces.jar -Dnode=2:node2:false:localhost:17373:17374 -Dport=18082`

Run node3: `java -jar target/xoom-distributed-spaces.jar -Dnode=3:node3:false:localhost:17375:17376 -Dport=18083`

Create a new spaces entry on node1:
```
curl --request POST 'http://localhost:18081/spaces' \
  --header 'Content-Type: application/json' \
  --data-raw '{"key": "key1", "value": "data1"}'
```

Check replicated entry on the other nodes:
```
curl --request GET 'http://localhost:18082/spaces/key1'
curl --request GET 'http://localhost:18083/spaces/key1'
```

Create a new spaces entry on node2:
```
curl --request POST 'http://localhost:18082/spaces' \
  --header 'Content-Type: application/json' \
  --data-raw '{"key": "key2", "value": "data2"}'
```

Check replicated entry on the other nodes:
```
curl --request GET 'http://localhost:18081/spaces/key2'
curl --request GET 'http://localhost:18083/spaces/key2'
```

Create a new spaces entry on node3:
```
curl --request POST 'http://localhost:18083/spaces' \
  --header 'Content-Type: application/json' \
  --data-raw '{"key": "key3", "value": "data3"}'
```

Check replicated entry on the other nodes:
```
curl --request GET 'http://localhost:18081/spaces/key3'
curl --request GET 'http://localhost:18082/spaces/key3'
```

Delete entries is also available:

```
curl --request DELETE 'http://localhost:18081/spaces/key3
```

`key3` should be deleted from all the other nodes.