# vlingo-http-backservice

This is an example service that works in conjunction with the `vlingo-http-frontservice`. Please see the [README.md](https://github.com/vlingo/vlingo-examples/tree/master/vlingo-http-frontservice) from that project for usage instructions.

## Understanding http-backend 

http-backend is a "large" backend system that the client makes reactive http communiction with over [sse](https://en.wikipedia.org/wiki/Server-sent_events) 

* The client [http-frontend](https://github.com/vlingo/vlingo-examples/tree/master/vlingo-http-frontservice) 
can send a public token and get a pivate token back.
This is done by send+listen for response via [sse](https://en.wikipedia.org/wiki/Server-sent_events). 

* Sending/listen to service http://localhost:8082/tokens/{publicToken}

* But first client must listen to vaultstreams/tokens
 
```
curl --request GET \
    --header "X-Correlation-ID:300:PrivateTokenSynchronizerActor-tokens" --header "Accept:text/event-stream" \
    http://localhost:8082/vaultstreams/tokens


curl --request GET --header 'X-Correlation-ID:PrivateTokenSynchronizerActor-tokens' 'http://localhost:8082/tokens/jfhf90r8re978er88e,ndf!--88dh*'

```
## Usage of server components

The setup of HTTP-server via vlingo-http-plugin is simple one liners. This might require some understanding.

See: [http-server](docs/http-backend.pdf) - for sequence diagram to get a little overview. 