# vlingo-http-frontservice

This is an example service that manages users with profiles. This service collaborates with the
`vlingo-http-backservice` to retrieve private tokens for the given user public token.

Build the `vlingo-http-frontservice` as follows, and then execute the `jar`:

```
$ mvn clean package
...
$ java -jar target/vlingo-http-frontservice-0.7.0-jar-with-dependencies.jar
```

In a separate command window build and start the `vlingo-http-backservice`:

```
$ mvn clean package
...
$ java -jar target/vlingo-http-backservice-0.7.0-jar-with-dependencies.jar
```

The following is a sample `curl` command that can be used to create a user on the `vlingo-http-frontservice` and start the process of retrieving the user's private token.

```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"nameData":{"given":"Jane","family":"Doe"},"contactData":{"emailAddress":"jane.doe@vlingo.io","telephoneNumber":"+1 212-555-1212"},"publicSecurityToken":"jfhf90r8re978er88e,ndf!--88dh*"}' http://localhost:8081/users
```

This command response with a similar result as follows:

```
HTTP/1.1 201 Created
Location: /users/175
Content-Length: 198

{"id":"175","nameData":{"given":"Jane","family":"Doe"},"contactData":{"emailAddress":"jane.doe@vlingo.io","telephoneNumber":"+1 212-555-1212"},"publicSecurityToken":"jfhf90r8re978er88e,ndf!--88dh*"}
```

Within a short time a `GET` on the `Location` header `URI` will retrieve the user, now with a confirmed private token.

```
$ curl -i -X GET -H "Content-Type: application/json" http://localhost:8081/users/175
```

This responses with a result similar to the following:

```
HTTP/1.1 200 OK
Content-Length: 198

{"id":"175","nameData":{"given":"Jane","family":"Doe"},"contactData":{"emailAddress":"jane.doe@vlingo.io","telephoneNumber":"+1 212-555-1212"},"publicSecurityToken":"jfhf90r8re978er88e,ndf!--88dh*"}
```

Try it yourself.