package io.vlingo.xoom.examples.pingpong.domain;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Config {
	public static String nodeName = "undefined";

	public static final Map<String, String> nodesConfig = Stream.of(new String[][] {
					{"node1", "1:node1:true:localhost:37371:37372"},
					{"node2", "2:node2:false:localhost:37373:37374"},
					{"node3", "3:node3:false:localhost:37375:37376"}})
					.collect(Collectors.toMap(entry -> entry[0], row -> row[1]));
}
