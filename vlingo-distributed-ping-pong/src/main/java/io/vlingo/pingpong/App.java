package io.vlingo.pingpong;

import io.vlingo.actors.Grid;
import io.vlingo.pingpong.domain.PingPongReferee;
import io.vlingo.pingpong.domain.impl.PingPongRefereeActor;
import io.vlingo.pingpong.domain.Ponger;
import io.vlingo.pingpong.domain.impl.PongerActor;

public class App {

  public static void main(final String[] args) throws Exception {
    final String nodeName = parseNameFromArguments(args);

    final Grid grid = Grid.start("world-of-ping-pong", nodeName);

    final Ponger ponger = grid.actorFor(Ponger.class, PongerActor.class);

    final PingPongReferee pingPongReferee = grid.actorFor(PingPongReferee.class,
        PingPongRefereeActor.class);

    pingPongReferee.whistle(nodeName)
        .andThenConsume(pinger -> pinger.ping(ponger, nodeName));
  }

  private static String parseNameFromArguments(String[] args) {
    if (args.length == 0) {
      System.err.println("The node must be named with a command-line argument.");
      System.exit(1);
    }
    else if (args.length > 1) {
      System.err.println("Too many arguments; provide node name only.");
      System.exit(1);
    }
    return args[0];
  }

}
