# vlingo-auth-authservice Example
## How-to Build
```
cd $VLINGO_HOME/vlingo-examples
mvn clean install
```

or

```
cd $VLINGO_HOME/vlingo-examples/vlingo-auth-authservice
mvn clean install
```

## How-to Start 
```
cd $VLINGO_HOME/vlingo-examples/vlingo-auth-authservice
java -jar target/vlingo-auth-authservice-[version]-jar-with-dependencies.jar
```

## How-to Interact

E.g., POST with curl to create a tenant

```
curl -i -X POST -H "Content-Type: application./json" -d '{"active":true,"description":"Core vlingo open source software initiative developers and sponsor","name":"vlingo Core SSO","tenantId":"1"}' http://localhost:8080/tenants
```

Expected Result:

```
HTTP/1.1 201 Created
Location: /tenants/84755693-9c4a-4984-abf7-3ab5f9ead33d
Content-Length: 173

{"active":true,"description":"Core vlingo open source software initiative developers and sponsor","name":"vlingo Core SSO","tenantId":"84755693-9c4a-4984-abf7-3ab5f9ead33d"}
```

Notice that the tenantId is generated and returned.