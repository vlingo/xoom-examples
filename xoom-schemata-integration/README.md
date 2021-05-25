# Integration With VLINGO XOOM SCHEMATA

XOOM SCHEMATA enables the management of schemas for events and commands that are to be consumed by other services and applications (e.g. Bounded Contexts), thus implementing the Published Language pattern.

XOOM SCHEMATA can be run as a standalone service with a user interface and by means of an HTTP API providing schema management from builds or command line interaction. The build interfaces requires the use of `xoom-build-plugins`. You can push schemas to and pull schemas from the registry, and generate source code for the schemas.

In projects you'll typically consume and/or publish schemas to integrate with other services and applications (e.g. Bounded Contexts) in a type safe and versioned manner.

In this example, you'll see how to:

* build and run `vlingo/xoom-schemata`
* manage schemata and schema meta data using the UI and the HTTP API
* push schemata to the registry from maven builds
* consume schemata stored in the registry as part of maven builds

For detailed information on the rationale and usage of XOOM SCHEMATA, refer to the documentation of [SCHEMATA](https://docs.vlingo.io/xoom-schemata) and the [build plugins](https://docs.vlingo.io/xoom-build-plugins).

## Prerequisites

To be able to follow along, you'll need:

* JDK 8
* Maven 3.3.x
* A running instance of XOOM SCHEMATA (see [below](#setting-up-xoom-schemata))
* Some master data (see [Provision Schemata Master Data](#provision-schemata-master-data))

### Setting Up XOOM SCHEMATA

The easiest way to get started is to use the [`vlingo/xoom-schemata` docker image](https://hub.docker.com/r/vlingo/xoom-schemata):

```
$ docker run -eXOOM_ENV=dev -p9019:9019 vlingo/xoom-schemata
```

This will expose the application on `:9019` using an in-memory database for persistence.

If you want to build SCHEMATA yourself:

```
$ clone https://github.com/vlingo/xoom-schemata
```

Refer to [Build](https://github.com/vlingo/xoom-schemata#build) for further instructions (`mvn package -Pfrontend` on JDK8 should do the trick).

For further configuration options like connecting to a persistent database, see [`vlingo/xoom-schemata` on Docker Hub](https://hub.docker.com/r/vlingo/xoom-schemata).

### Provision Schemata Master Data

To integrate your projects with the schema registry, you need to make your organization structure and available published languages known first. You can do this either via the [UI](#using-the-ui) or the [HTTP API](#using-the-api).

Schemas are structured in a hierarchical manner:
`Organisation -> Units -> Contexts -> Schemas -> Schema Versions`
We'll push new schema versions using the maven plugin.
Before running the build, we will prepare at least one of each entity.

#### Using the UI

From the navigation drawer to the left, open the editor for each of these entities and create one.
The build assumes the following structure:

* Organisation: `VLINGO`
	* Unit: `examples`
		* Context Namepace: `io.vlingo.examples.schemata`
			* Schema: `SchemaDefined`
			* Schema: `SchemaPublished`

For help on using the UI, [the docs have got you covered](https://docs.vlingo.io/xoom-schemata#using-the-gui). Apply this for the above mentioned structure.

#### Using the API

In case you are using IntelliJ, you can just run the requests in [`masterdata_intellij.http`](https://github.com/vlingo/xoom-examples/blob/9e28e163d8873e6ce0b73aced3b3ffe7f92e246b/xoom-schemata-integration/masterdata_intellij.http) directly. If you are using VSCode, you can run [`masterdata_vscode.http`](https://github.com/vlingo/xoom-examples/blob/9e28e163d8873e6ce0b73aced3b3ffe7f92e246b/xoom-schemata-integration/masterdata_vscode.http) (with the extension mentioned there) instead.
If you don't use any of these, `masterdata.sh` is a bash script doing the same, but requires `curl` and `jq` on the PATH and `XOOM_SCHEMATA_PORT` to be set, e.g. `XOOM_SCHEMATA_PORT=9019 ./masterdata.sh`.
Otherwise, deriving the `wget` calls from the snippets should be a simple exercise, or you could even use `Postman`, `HTTPie`, `Insomnia`, or `Invoke-WebRequest`. It's your choice.

## Run

Now that there is master data in place, we can publish some schemas from
`xoom-schemata-producer`. The schema sources are within the project, inside
`src/main/vlingo/schemata`. To publish the schemas to the registry,
simply run `mvn install` in the project root.
The build output should contain something like:
```
[INFO] --- xoom-build-plugins:0.9.3-RC4:push-schema (default) @ xoom-schemata-producer ---
[INFO] vlingo/maven: Pushing project schemata to xoom-schemata registry.
[INFO] Pushing Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 to http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1.
[INFO] Successfully pushed http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1
[INFO] Setting source to SchemaPublished.vss for Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1
[INFO] Pushing Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1 to http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1.
[INFO] Successfully pushed http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1
```
The UI should display the created schemas, similar to this:
![home page of the VLINGO/SCHEMATA UI](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM21620_xboVuiBgbJw%2F-MM29DWTgqgr-rH17m5H%2FschemataHomeSpec.png)
Only the hierarchy elements will be different, as defined in [Using the UI](#using-the-ui).
You can also have a look at the generated code (`Specification -> Source`) and the generated description (`Description -> Preview`).


Next, hop over to `xoom-schemata-consumer` and open `SchemataUsageTest` in your IDE.
You'll notice that it does not compile, as the schema class is missing.
Now run `mvn generate-sources` and verify that the code generated from schemata is pulled and written to `target/generated-sources/vlingo`:
```
[INFO] --- xoom-build-plugins:1.0.0:pull-schema (pull) @ xoom-schemata-consumer ---
[INFO] vlingo/maven: Pulling code generated from vlingo/schemata registry.
[INFO] SchemataService{url=http://localhost:9019, clientOrganization='Vlingo', clientUnit='examples'}
[INFO] Retrieving version data for Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 from http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1/status
[WARNING] Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 status is 'Draft': don't use in production builds
[INFO] Pulling Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 from http://localhost:9019/code/Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1/java
[INFO] Pulled Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1
[INFO] Writing Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 to /private/tmp/xoom-examples/xoom-schemata-integration/xoom-schemata-consumer/target/generated-sources/vlingo/io/vlingo/examples/schemata/event/SchemaDefined.java
[INFO] Wrote /private/tmp/xoom-examples/xoom-schemata-integration/xoom-schemata-consumer/target/generated-sources/vlingo/io/vlingo/examples/schemata/event/SchemaDefined.java
[INFO] Retrieving version data for Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1 from http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1/status
[WARNING] Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1 status is 'Draft': don't use in production builds
[INFO] Pulling Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1 from http://localhost:9019/code/Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1/java
[INFO] Pulled Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1
[INFO] Writing Vlingo:examples:io.vlingo.examples.schemata:SchemaPublished:0.0.1 to /private/tmp/xoom-examples/xoom-schemata-integration/xoom-schemata-consumer/target/generated-sources/vlingo/io/vlingo/examples/schemata/event/SchemaPublished.java
[INFO] Wrote /private/tmp/xoom-examples/xoom-schemata-integration/xoom-schemata-consumer/target/generated-sources/vlingo/io/vlingo/examples/schemata/event/SchemaPublished.java
```
The tests that depend on schema sources will now compile and execute as expected. Verify this by running `mvn test`.

What we have just done is just for illustration purposes. Typically, you'll rely on maven's default lifecycle binding and not call `generate-sources` explicitly. Simply running `mvn install` will determine that `xoom-build-plugin` is bound to `generate-sources` and run it before compilation.

In the build output you will see two warning messages indicating that the status of the schema used is `Draft`. You can go through the schema's lifecycle using the UI:
![schema version lifecycle](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM21620_xboVuiBgbJw%2F-MM29DWTgqgr-rH17m5H%2FschemataHomeSpec.png)
Alternatively, you can change the status using the API by `PATCH`ing against:

```
http://localhost:9019/api/organizations/<orgId>/units/<unitId>/contexts/<contextId>/schemas/<schemaId>/versions/<schemaVersionId>/status
```

Now, publish one of the schemas and re-run the consumer build. You'll notice the warning was eliminated. Now deprecate the schema, and re-build again. You'll see a warning again. After setting the state to removed, your consumer build will fail, try that as well:

```
[INFO] --- xoom-build-plugins:1.0.0:pull-schema (pull) @ xoom-schemata-consumer ---
[INFO] vlingo/maven: Pulling code generated from vlingo/schemata registry.
[INFO] SchemataService{url=http://localhost:9019, clientOrganization='Vlingo', clientUnit='examples'}
[INFO] Retrieving version data for Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 from http://localhost:9019/versions/Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1/status
[ERROR] Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 status is 'Removed' and may no longer be used
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.773 s
[INFO] Finished at: 2020-01-17T09:54:30+01:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal io.vlingo.xoom:xoom-build-plugins:1.0.0:pull-schema (pull) on project xoom-schemata-consumer: Vlingo:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.1 has reached the end of its life cycle -> [Help 1]
```

XOOM SCHEMATA also provides a safety net to prevent you from publishing incompatible versions by accident, so you won't break consumers of your published versions.
To see what happens in this case, open the `SchemaDefined.vss` specification and make some incompatible changes, e.g. by changing a type, reordering some fields and changing field names:
```
event SchemaDefined {
    long occurredOn
    version v
    type t
}
```
(see [SchemaDefined2.vss](https://github.com/vlingo/xoom-examples/blob/9e28e163d8873e6ce0b73aced3b3ffe7f92e246b/xoom-schemata-integration/xoom-schemata-producer/src/main/vlingo/schemata/SchemaDefined2.vss))

In the producer project's `pom.xml`, remove the second schema from the list of
schemas to push and reference the updated file as a new patch version:
```
<schema>
  <!-- updated reference to new version -->
  <ref>VLINGO:examples:io.vlingo.examples.schemata:SchemaDefined:0.0.2</ref>
  <!-- use updated specification; this would normally be the same file -->
  <src>SchemaDefined2.vss</src>
  <!-- specify which version the update is applied to -->
  <previousVersion>0.0.1</previousVersion>
</schema>
```
You'll notice that the build fails and presents you with a list of changes you have made. Now you can either change the update to make it compatible or update the version to the next major version, `1.0.0`.

By using the schema registry you not only have a way to integrate multiple bounded contexts, but are, as a **consumer**, safe from inadvertent upstream incompatibilities and, as a **publisher**, safe from accidentally pushing such updates.

If you've tried this via the UI, you would have seen a detailed incompatible diff, like this:

![ui-incompatible-diff](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM2SfgJQuqf4zd8gjY-%2F-MM2Zaz2dmErAN74k9mO%2FschemataNewSchema.png)
