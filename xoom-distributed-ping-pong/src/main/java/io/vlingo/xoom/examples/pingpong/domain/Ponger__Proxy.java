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
import io.vlingo.xoom.examples.pingpong.domain.Ponger;
import java.lang.String;

public class Ponger__Proxy extends ActorProxyBase<io.vlingo.xoom.examples.pingpong.domain.Ponger> implements io.vlingo.xoom.examples.pingpong.domain.Ponger {

  private static final String pongRepresentation1 = "pong(java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public Ponger__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.examples.pingpong.domain.Ponger.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Ponger__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public void pong(java.lang.String arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<Ponger> self = this;
      final SerializableConsumer<Ponger> consumer = (actor) -> actor.pong(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, Ponger.class, consumer, null, pongRepresentation1); }
      else { mailbox.send(new LocalMessage<Ponger>(actor, Ponger.class, consumer, pongRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pongRepresentation1));
    }
  }
}
