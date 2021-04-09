package io.vlingo.xoom.examples.pingpong.domain;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Definition.SerializationProxy;
import io.vlingo.xoom.actors.ActorProxyBase;
import io.vlingo.xoom.actors.DeadLetter;
import io.vlingo.xoom.actors.LocalMessage;
import io.vlingo.xoom.actors.Mailbox;
import io.vlingo.xoom.actors.Returns;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.SerializableConsumer;
import io.vlingo.xoom.examples.pingpong.domain.Pinger;
import io.vlingo.xoom.examples.pingpong.domain.Ponger;
import java.lang.String;

public class Pinger__Proxy extends ActorProxyBase<io.vlingo.xoom.examples.pingpong.domain.Pinger> implements io.vlingo.xoom.examples.pingpong.domain.Pinger {

  private static final String pingRepresentation1 = "ping(io.vlingo.xoom.examples.pingpong.domain.Ponger, java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public Pinger__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.examples.pingpong.domain.Pinger.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Pinger__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public void ping(io.vlingo.xoom.examples.pingpong.domain.Ponger arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<Pinger> self = this;
      final SerializableConsumer<Pinger> consumer = (actor) -> actor.ping(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, Pinger.class, consumer, null, pingRepresentation1); }
      else { mailbox.send(new LocalMessage<Pinger>(actor, Pinger.class, consumer, pingRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pingRepresentation1));
    }
  }
}
