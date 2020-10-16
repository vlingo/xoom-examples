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
import io.vlingo.pingpong.domain.Mailer;
import io.vlingo.pingpong.domain.Ponger;
import java.lang.String;

public class Mailer__Proxy extends ActorProxyBase<io.vlingo.pingpong.domain.Mailer> implements io.vlingo.pingpong.domain.Mailer {

  private static final String sendRepresentation1 = "send(io.vlingo.pingpong.domain.Ponger, java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public Mailer__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.pingpong.domain.Mailer.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Mailer__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public void send(io.vlingo.pingpong.domain.Ponger arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<Mailer> self = this;
      final SerializableConsumer<Mailer> consumer = (actor) -> actor.send(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, Mailer.class, consumer, null, sendRepresentation1); }
      else { mailbox.send(new LocalMessage<Mailer>(actor, Mailer.class, consumer, sendRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, sendRepresentation1));
    }
  }
}
