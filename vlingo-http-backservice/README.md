# vlingo-http-backservice

This is an example service that works in conjunction with the `vlingo-http-frontservice`. Please see the [README.md](https://github.com/vlingo/vlingo-examples/tree/master/vlingo-http-frontservice) from that project for usage instructions.

## Understanding http-backend 

http-backend is a "large" backend system that the client makes reactive http communication with via [Server Send Events](https://en.wikipedia.org/wiki/Server-sent_events) 

* The client [http-frontend](https://github.com/vlingo/vlingo-examples/tree/master/vlingo-http-frontservice) 
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
data: 4444:001
data: $s0$e0801$8esMZDbUbnKB6pjgMRWH8A==$fcb1brZpgrt3W3AYos4Zd6bmPWiWNWYdO0vebC4kJgU=

```  


## GeneratePrivateToken

* GeneratePrivateToken is asked to service by http://localhost:8082/tokens/{publicToken}
* The correlation Id is SSE back the `vaultstream`

```
curl --request GET \
    --header 'X-Correlation-ID:444:001' \
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


## Usage of server components

The setup of HTTP-server via vlingo-http-plugin is simple one liners. This might require some understanding.

See: [http-server](docs/http-backend.pdf) - for sequence diagram to get a little overview. 