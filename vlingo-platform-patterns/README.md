# vlingo-platform-patterns 

## Steps to run vlingo/http server using `Bootstrap`

```
mvn clean package        
mvn exec:java
```


## Demo objectives

There is no live behind this rest service. The service communicate strings. So it is more a 'text' service

The demo shows;

* How to make a HTTP Server
* How to make a stop via shutdown hook
* How to add web resource to the server with routes()
* How to log outside an Actor
* How to return response via class Completes

## Client requests with curl

The idea is to declare organizations. First you need to get an organization id. Then you can patch this organization.
You can give the organization a name. You can disable and enable the organization.

Below we do some steps. Running in bash. 

1. Request an id. Do a http POST 
2. Add a name FIFA - using http PATCH with the name in body. The Content-Type is json. But the sample is simpler than that - just text
3. disable organization - using http PATCH with url argument
4. enable organization - using http PATCH with url argument
5. get organization - using default http GET
 
```
curl --include --request POST http://localhost:8081/organizations


# Assign variable to the POST output
# Should be something like LOCATION=/organizations/c3a35b98-336c-4303-ba08-04c5844f065f
LOCATION=location from above

# Now tell that Orgainization is FIFA
#
curl --request PATCH --header 'Content-Type: application/json' --data 'FIFA'  http://localhost:8081$LOCATION/name


# Now tell that Orgainization is disabled and later enabled
# You give the command 'disable' - the system makes event 'disabled'
curl --request PATCH  http://localhost:8081$LOCATION/disable
curl --request PATCH  http://localhost:8081$LOCATION/enable

# 
curl -l --include --request GET http://localhost:8081$LOCATION

```

