# xoom-http-backservice

This is an example service that works in conjunction with the `xoom-http-frontservice`. Please see the [README.md](https://github.com/vlingo/xoom-examples/tree/master/xoom-http-frontservice) from that project for usage instructions.

Here is a naive diagram of  [private-token-reactive-service](docs/private-token-reactive-service.jpg)

## Understanding http-backend 

http-backend is a "large" backend system that the client makes reactive http communication with via [Server Send Events](https://en.wikipedia.org/wiki/Server-sent_events) 

* The client [http-frontend](https://github.com/vlingo/xoom-examples/tree/master/xoom-http-frontservice) 
can send a public token and get a pivate token back.
This is done by send+listen for response via [SSE](https://en.wikipedia.org/wiki/Server-sent_events). 

## Listening to vaultstreams - and keep listening

* start listening to service http://localhost:8082/vaultstreams/tokens - and keep listening
* `curl .. &` - set request into background

```
# vaultstreams listening - and keep listening
curl --request GET \
    --header "X-Correlation-ID:PrivateTokenSynchronizerActor-tokens" --header "Accept:text/event-stream" \
    http://localhost:8082/vaultstreams/tokens &

```

* The listening curl above will stay connected and get an SSE each time such an event is ready. This means
 each time `GeneratePrivateToken` is executed. SSE looks like the sample below

```
id: 1
event: PrivateTokenGenerated
data: 245:UserState:245
data: $s0$e0801$8esMZDbUbnKB6pjgMRWH8A==$fcb1brZpgrt3W3AYos4Zd6bmPWiWNWYdO0vebC4kJgU=

```  


## GeneratePrivateToken

* GeneratePrivateToken is asked to service by http://localhost:8082/tokens/{publicToken}
* The correlation Id is SSE back the `vaultstream`

```
curl --request GET \
    --header 'X-Correlation-ID:245:UserState:245' \
    'http://localhost:8082/tokens/jfhf90r8re978er88e,ndf!--88dh*' &

```

## Stop vaultstreams listening

* stop listening by using the same resource + 'id' as the listening was started with

```

# stop listening - note id=1
curl --request DELETE \
    --header "X-Correlation-ID:PrivateTokenSynchronizerActor-tokens" --header "Accept:text/event-stream" \
    http://localhost:8082/vaultstreams/tokens/1

```

## Connection: keep-alive

As you might notice - the `curl` command does not return. This is because the server does not end the response.
 The basic idea is that client should read again and again `repeat()`
 
```
// code from client's class io.vlingo.xoom.examples.frontservice.infra.projection.PrivateTokenSynchronizerActor

  private void subscribeToEvents(final Client client) {
    client.requestWith(
            Request
              .has(GET)
              .and(URI.create("/vaultstreams/tokens"))
  ....
            })
            .repeat();
  }

```
The **.repeat()** in the above code makes the client continue and read the stream repeatably.


## SseStreamResourceDispatcher got no context.completes()

Http-backend's resource dispatcher that handles ressource URI `/vaultstreams/tokens` does 
NOT return a response status code. This means that client will experience Connection: keep-alive 

```
// the context.completes statement is NOT used in http-backends dispatcher
 
        context.completes.with(Response.of(Ok, "Now I return"));

```


## Usage of server components

The setup of HTTP-server via xoom-http-plugin is a simple one-liners. This might require some understanding.

See: [http-server](docs/http-backend.pdf) - for sequence diagram to get a little overview. 


### Example Improvements

The example gives a good understanding of SSE. 

It is an early example. Therefore there there are room for improvement. 

* The definition of Server is relative specific coded. Might need a refactoring - so the statements that 
defines `Server` should specify the resources that is used a little more explicitly.

* Http-backend must start before http-frontend. http-frontend will not start correct when backend is missing.
* Similar http-frontend shoudl be able to survive restart of a http-backend
* GeneratePrivateToken will be added to a list in EventJournalActor. So running the system for a long time will 
cause memory usage to increase.     
