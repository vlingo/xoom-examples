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

## How-to Load
A `jmeter` directory has been added that contains a load script, `authDataBootstrap.jmx` as well as a data directory with sub-directories containing CSV files for: tenant, group, role, and permissions.  Ten tenants worth of data are included and written into the JMeter script.

In order to load data:

1. Start authservice as described above.
2. [Download Apache JMeter](https://jmeter.apache.org) for your OS.
3. Start Apache JMeter according to your OS.
4. It's important to navigate to the JMeter script so that a present-working-directory is established as data is accessed relative to this direcctory.  Open `authDataBootstrap.jmx` in this directory vlingo-auth-authservice/jmeter/.
5. Click the `start` icon (green triangle).

Each of ten tenants data is loaded by way of 10 separate threads named `Load Tenant One`, `Load Tenant Two`, etc.  Spin down and click on each of the elements of the threads to see how this was setup.

Access to the result of the load is in a number of locations.

1. A top level `Summary Report` displays overall runtime and throughput information.
2. Data specific to each of the Tenant loads is found in the `View Results in Table`, and `View Results Tree` within each thread.  Of the two, `View Results Tree` is more valuable for finding `tenantId` instances in either the `Request` or `Response Data` tabs of each individual request-response displayed in the `View Results Tree` portion of the JMeter UI.
 