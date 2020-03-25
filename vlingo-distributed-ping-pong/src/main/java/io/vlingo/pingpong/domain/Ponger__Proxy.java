package io.vlingo.pingpong.domain;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition.SerializationProxy;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.actors.Returns;
import io.vlingo.common.Completes;
import io.vlingo.common.SerializableConsumer;
import io.vlingo.pingpong.domain.Ponger;
import io.vlingo.pingpong.domain.Pinger;
import java.lang.String;

public class Ponger__Proxy extends ActorProxyBase<io.vlingo.pingpong.domain.Ponger> implements io.vlingo.pingpong.domain.Ponger {

  private static final String pongRepresentation1 = "pong(io.vlingo.pingpong.domain.Pinger, java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public Ponger__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.pingpong.domain.Ponger.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Ponger__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public void pong(io.vlingo.pingpong.domain.Pinger arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<Ponger> self = this;
      final SerializableConsumer<Ponger> consumer = (actor) -> actor.pong(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, Ponger.class, consumer, null, pongRepresentation1); }
      else { mailbox.send(new LocalMessage<Ponger>(actor, Ponger.class, consumer, pongRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pongRepresentation1));
    }
  }
}
