// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.kubernetescluster;

import io.vlingo.xoom.cluster.model.application.ClusterApplicationAdapter;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.Collection;

public class KubernetesClusterApplicationActor extends ClusterApplicationAdapter {
    // private AttributesProtocol client;
    private final Node localNode;

    public KubernetesClusterApplicationActor(final Node localNode) {
        this.localNode = localNode;
    }

    @Override
    public void start() {
        logger().debug("APP: KubernetesClusterApplication started on node: " + localNode);
    }

    @Override
    public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
        for (final Node id : liveNodes) {
            logger().debug("APP: Live node confirmed: " + id);
        }
        printHealthy(isHealthyCluster);
    }

    @Override
    public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
        logger().debug("APP: " + nodeId + " joined cluster");
        printHealthy(isHealthyCluster);
    }

    @Override
    public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
        logger().debug("APP: " + nodeId + " left cluster");
        printHealthy(isHealthyCluster);
    }

    @Override
    public void informClusterIsHealthy(boolean isHealthyCluster) {
        printHealthy(isHealthyCluster);
    }

    private void printHealthy(final boolean isHealthyCluster) {
        if (isHealthyCluster) {
            logger().debug("APP: Cluster is healthy");
        } else {
            logger().debug("APP: Cluster is NOT healthy");
        }
    }
}
