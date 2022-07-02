package io.vlingo.xoom.examples.patterns.entity;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ClusterProperties {
  private static final Random random = new Random();
  private static AtomicInteger PORT_TO_USE = new AtomicInteger(10_000 + random.nextInt(50_000));

  public static io.vlingo.xoom.cluster.model.Properties allNodes() {
    java.util.Properties properties = new java.util.Properties();

    properties = common(allOf(properties), 3);

    final io.vlingo.xoom.cluster.model.Properties clusterProperties =
            io.vlingo.xoom.cluster.model.Properties.openForTest(properties);

    return clusterProperties;
  }

  public static io.vlingo.xoom.cluster.model.Properties oneNode() {
    java.util.Properties properties = new java.util.Properties();

    properties = common(oneOnly(properties), 1);

    final io.vlingo.xoom.cluster.model.Properties clusterProperties =
            io.vlingo.xoom.cluster.model.Properties.openForTest(properties);

    return clusterProperties;
  }

  private static java.util.Properties oneOnly(final java.util.Properties properties) {
    properties.setProperty("node.node1.id", "1");
    properties.setProperty("node.node1.name", "node1");
    properties.setProperty("node.node1.host", "localhost");
    properties.setProperty("node.node1.op.port", nextPortToUseString());
    properties.setProperty("node.node1.app.port", nextPortToUseString());

    return properties;
  }

  private static java.util.Properties allOf(final java.util.Properties properties) {
    oneOnly(properties);

    properties.setProperty("node.node1.seed", "true");
    properties.setProperty("cluster.nodes.quorum", "2");

    properties.setProperty("node.node2.id", "2");
    properties.setProperty("node.node2.name", "node2");
    properties.setProperty("node.node2.host", "localhost");
    properties.setProperty("node.node2.op.port", nextPortToUseString());
    properties.setProperty("node.node2.app.port", nextPortToUseString());

    properties.setProperty("node.node3.id", "3");
    properties.setProperty("node.node3.name", "node3");
    properties.setProperty("node.node3.host", "localhost");
    properties.setProperty("node.node3.op.port", nextPortToUseString());
    properties.setProperty("node.node3.app.port", nextPortToUseString());

    return properties;
  }

  private static java.util.Properties common(final java.util.Properties properties, final int totalNodes) {
    properties.setProperty("cluster.ssl", "false");

    properties.setProperty("cluster.op.buffer.size", "4096");
    properties.setProperty("cluster.app.buffer.size", "10240");
    properties.setProperty("cluster.op.outgoing.pooled.buffers", "20");
    properties.setProperty("cluster.app.outgoing.pooled.buffers", "50");

    properties.setProperty("cluster.msg.charset", "UTF-8");

    properties.setProperty("cluster.app.class", "io.vlingo.xoom.cluster.model.application.FakeClusterApplicationActor");
    properties.setProperty("cluster.app.stage", "fake.app.stage");

    properties.setProperty("cluster.health.check.interval", "2000");
    properties.setProperty("cluster.live.node.timeout", "20000");
    properties.setProperty("cluster.heartbeat.interval", "7000");
    properties.setProperty("cluster.quorum.timeout", "60000");

    if (totalNodes == 1) {
      properties.setProperty("cluster.nodes", "node1");
    } else if (totalNodes == 3) {
      properties.setProperty("cluster.nodes", "node1,node2,node3");
    } else {
      throw new IllegalArgumentException("The totalNodes must be 1 or 3.");
    }

    return properties;
  }

  private static int nextPortToUse() {
    return PORT_TO_USE.incrementAndGet();
  }

  private static String nextPortToUseString() {
    return "" + nextPortToUse();
  }
}
