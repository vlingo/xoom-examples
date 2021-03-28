// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.kubernetescluster;

import io.vlingo.actors.World;
import io.vlingo.cluster.NodeBootstrap;
import io.vlingo.cluster.model.Properties;

public class Bootstrap {

    public static void main(final String[] args) throws Exception {
        final String nodeName = parseNodeName(args);
        final World world = World.start("kubernetes-cluster");
        NodeBootstrap.boot(world, new KubernetesClusterInstantiator(), Properties.instance(), nodeName, false);
    }

    private static String parseNodeName(final String[] args) {
        if (args.length == 0) {
            System.err.println("The node must be named with a command-line argument.");
            System.exit(1);
        } else if (args.length > 1) {
            System.err.println("Too many arguments; provide node name only.");
            System.exit(1);
        }
        return args[0];
    }

}
