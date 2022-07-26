# xoom-platform-patterns 

## Steps to run VLINGO XOOM Http server using `Bootstrap`

```
cd xoom-platform-patterns
mvn clean package        
mvn exec:java
```


## Demo objectives

There is no live behind this rest service. The service communicate strings. So it is more a 'text' service

The demo shows;

* How to make an HTTP Server
* How to make a stop via shutdown hook
* How to add web resource to the server with routes()
* How to log outside an Actor
* How to return response via class Completes
* How to open a new web server
* How to close an existing web server

## Client requests with curl - step 1

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

# Admin Demo - step 2

XOOM implements DDD design pattern - the hexagonal architecture. See [domain-driven-design-and-hexagonal-architecture](https://www.slideshare.net/crishantha/domain-driven-design-and-hexagonal-architecture) look at slide 1 and slide 35.

The objective is to demonstrate XOOM's ability to add web-servers - simple **Actor** in the role as **Adapter**

* On port 9090 runs the administrator
* The actual **Organizational** service runs on port 8081. See step 1.

The application demonstrates how to 'start' web servers and how to 'stop' web servers again. 
The admin can list the running web servers.

```
curl --request GET http://localhost:9090/admin
# expect to see two portnumbers

curl --request POST http://localhost:9090/admin/server/8082
curl --request POST http://localhost:9090/admin/server/8083
curl --request GET http://localhost:9090/admin
# expect to see four portnumbers

curl --request GET http://localhost:8081/info
curl --request GET http://localhost:8082/info
curl --request GET http://localhost:8083/info
# expect to see the correct portnumber

curl --request DELETE http://localhost:9090/admin/server/8082
curl --request DELETE http://localhost:9090/admin/server/8083
curl --request GET http://localhost:9090/admin

curl --request DELETE http://localhost:9090/admin/world
# application stops

```


