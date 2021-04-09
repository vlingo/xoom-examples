# vlingo-distributed-ping-pong

A simple example to demo vlingo/lattice Grid features, running on three vlingo/cluster nodes.

## Building and Running In JVM Only

Start a console/command window so you can build the `vlingo-distributed-ping-pong` and start it by executing the built `jar`.

Note: The following version number may have changed since this README was written. The `jar` will be generated with the latest
vlingo/PLATFORM version number.

```
$ mvn clean package
...
console1$ java -jar target/vlingo-distributed-ping-pong-1.2.20-SNAPSHOT.jar node1
```

Following this, open two more console/command windows, `cd` to the project directory, and in each console/command window start
a different node.

```
console2$ java -jar target/vlingo-distributed-ping-pong-1.2.20-SNAPSHOT.jar node2
...
console3$ java -jar target/vlingo-distributed-ping-pong-1.2.20-SNAPSHOT.jar node3
```

Observe the ping-pong played across nodes. The game distribution will depend on the actor addresses generated for each
actor. In any given id set, expect the possibility that everything will run on one node. Yet, it is likely to run on
two or three nodes. The actor address generation is completely random, and thus unpredictable.


## Building and Running In Kubernetes

To test the example, run `mvn clean compile` from the root of the project.

Then, start three terminal windows and run one of the following commands in each.

```
mvn exec:java -Dexec.args="node1"
```
```
mvn exec:java -Dexec.args="node2"
```
```
mvn exec:java -Dexec.args="node3"
```

You may want to configure the ports for the communication channels between the nodes in the cluster in `src/main/resources/vlingo-cluster.properties`.

A successful run should output the similar logs as the following:

```
...
Referee GridAddress[id=d14b8fe5-cc0e-4b04-8f82-29b0e25cf0f7, name=(none)] whistling start from node1
Pinger::ping::node1::GridAddress[id=ec160f38-9adb-4279-be09-feb578c5b25e, name=(none)]
Ponger::pong::node1::GridAddress[id=73717fcd-d6cc-4957-9e3d-a3200ea44db3, name=(none)]
Pinger::ping::node1::GridAddress[id=ec160f38-9adb-4279-be09-feb578c5b25e, name=(none)]
Ponger::pong::node1::GridAddress[id=73717fcd-d6cc-4957-9e3d-a3200ea44db3, name=(none)]
...
``` 

- See project repository: https://github.com/vlingo/vlingo-examples

- See Grid documentation: https://docs.vlingo.io/vlingo-lattice/grid


## Licensing

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2021 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
