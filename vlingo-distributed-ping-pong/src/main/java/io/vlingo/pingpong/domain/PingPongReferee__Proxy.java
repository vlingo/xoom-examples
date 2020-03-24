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
import io.vlingo.pingpong.domain.PingPongReferee;
import io.vlingo.pingpong.domain.Pinger;
import io.vlingo.common.Completes;
import java.lang.String;

public class PingPongReferee__Proxy extends ActorProxyBase<io.vlingo.pingpong.domain.PingPongReferee> implements io.vlingo.pingpong.domain.PingPongReferee {

  private static final String whistleRepresentation1 = "whistle(java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public PingPongReferee__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.pingpong.domain.PingPongReferee.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public PingPongReferee__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public io.vlingo.common.Completes<io.vlingo.pingpong.domain.Pinger> whistle(java.lang.String arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<PingPongReferee> self = this;
      final SerializableConsumer<PingPongReferee> consumer = (actor) -> actor.whistle(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      final io.vlingo.common.Completes<io.vlingo.pingpong.domain.Pinger> returnValue = Completes.using(actor.scheduler());
      if (mailbox.isPreallocated()) { mailbox.send(actor, PingPongReferee.class, consumer, Returns.value(returnValue), whistleRepresentation1); }
      else { mailbox.send(new LocalMessage<PingPongReferee>(actor, PingPongReferee.class, consumer, Returns.value(returnValue), whistleRepresentation1)); }
      return returnValue;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, whistleRepresentation1));
    }
    return null;
  }
}
