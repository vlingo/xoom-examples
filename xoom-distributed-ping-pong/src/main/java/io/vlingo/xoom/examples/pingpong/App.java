package io.vlingo.xoom.examples.pingpong;

import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.examples.pingpong.domain.Config;
import io.vlingo.xoom.examples.pingpong.domain.Mailer;
import io.vlingo.xoom.examples.pingpong.domain.Pinger;
import io.vlingo.xoom.examples.pingpong.domain.Ponger;
import io.vlingo.xoom.examples.pingpong.domain.impl.MailerActor;
import io.vlingo.xoom.examples.pingpong.domain.impl.PingerActor;
import io.vlingo.xoom.examples.pingpong.domain.impl.PongerActor;

public class App {

  public static void main(final String[] args) throws Exception {
    Config.nodeName = parseNameFromArguments(args);
    final Grid grid = Grid.start("world-of-ping-pong", Config.nodesConfig.get(Config.nodeName));

    if ("node1".equals(Config.nodeName)) {
      spinUpActors(grid);
    }
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

  private static void spinUpActors(Grid grid) {
    final Pinger pinger = grid.actorFor(Pinger.class, PingerActor.class);
    final Mailer mailer = grid.actorFor(Mailer.class, MailerActor.class, pinger);
    final Ponger ponger = grid.actorFor(Ponger.class, PongerActor.class, mailer);

    pinger.ping(ponger, "bootstrap");
  }

}
