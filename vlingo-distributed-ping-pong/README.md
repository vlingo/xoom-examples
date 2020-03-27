# distributed-ping-pong

A simple example to demo vlingo/lattice Grid features, running on three vlingo/cluster nodes.

Start a console/command window so you can build the `vlingo-distributed-ping-pong` and start it by executing the built `jar`.

Note: The following version number may have changed since this README was written. The `jar` will be generated with the latest
vlingo/PLATFORM version number.

```
$ mvn clean package
...
console1$ java -jar target/vlingo-distributed-ping-pong-1.2.9.jar node1
```

Following this, open two more console/command windows, `cd` to the project directory, and in each console/command window start
a different node.

```
console2$ java -jar target/vlingo-distributed-ping-pong-1.2.9.jar node2
...
console3$ java -jar target/vlingo-distributed-ping-pong-1.2.9.jar node3
```

Observe the ping-pong played across nodes. The game distribution will depend on the actor addresses generated for each
actor. In any given id set, expect the possibility that everything will run on one node. Yet, it is likely to run on
two or three nodes. The actor address generation is completely random, and thus unpredictable.


License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2020 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.

