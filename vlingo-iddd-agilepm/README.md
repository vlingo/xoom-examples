# vlingo-iddd-agilepm 

## Steps to run

```
docker-compose up -d
mvn clean package        
java -jar target/vlingo-iddd-agilepm-0.9.1-RC2-jar-with-dependencies.jar
```

Don't forget to also start `vlingo-iddd-collaboration` server

## Operations
### Product
#### Create
```
curl -X POST http://localhost:8080/products \
-H "Content-Type: application/json" \
--data-binary @- << EOF
        {
            "tenantId" : "tenant_id_1",
            "ownerId" : "owner_id_1" ,
            "name" : "product_name",
            "description" : "product_description",
            "hasDiscussion" : true
        }
EOF

```