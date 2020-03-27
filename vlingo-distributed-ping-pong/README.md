# distributed-ping-pong

A simple example to demo vlingo/lattice Grid features, running on a three nodes vlingo/cluster.

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

repository: https://github.com/vlingo/vlingo-examples

Grid documentation: https://docs.vlingo.io/vlingo-lattice/grid

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2020 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.


### Licenses for Dependencies

MurmurHash.java is open source licensed under Apache 2 by the Apache Software Foundation
http://www.apache.org/licenses/LICENSE-2.0

See: https://github.com/apache/cassandra/blob/trunk/src/java/org/apache/cassandra/utils/MurmurHash.java
