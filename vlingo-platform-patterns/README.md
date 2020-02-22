# vlingo-platform-patterns 

## Steps to run vlingo/http server using `Bootstrap`

```
mvn clean package        
mvn exec:java
```

## Client requests with curl

```
curl -i -X POST http://localhost:8081/organizations

curl -l -i -X GET http://localhost:8081/organizations/{id-from-POST-Location-header}
```
