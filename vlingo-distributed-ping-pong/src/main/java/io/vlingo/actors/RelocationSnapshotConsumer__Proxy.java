package io.vlingo.actors;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition.SerializationProxy;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.actors.Returns;
import io.vlingo.common.Completes;
import io.vlingo.common.SerializableConsumer;
import io.vlingo.actors.RelocationSnapshotConsumer;
import java.lang.Object;

public class RelocationSnapshotConsumer__Proxy<S> extends ActorProxyBase<io.vlingo.actors.RelocationSnapshotConsumer> implements io.vlingo.actors.RelocationSnapshotConsumer<S> {

  private static final String applyRelocationSnapshotRepresentation1 = "applyRelocationSnapshot(S)";

  private final Actor actor;
  private final Mailbox mailbox;

  public RelocationSnapshotConsumer__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.actors.RelocationSnapshotConsumer.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public RelocationSnapshotConsumer__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public void applyRelocationSnapshot(S arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<RelocationSnapshotConsumer> self = this;
      final SerializableConsumer<RelocationSnapshotConsumer> consumer = (actor) -> actor.applyRelocationSnapshot(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RelocationSnapshotConsumer.class, consumer, null, applyRelocationSnapshotRepresentation1); }
      else { mailbox.send(new LocalMessage<RelocationSnapshotConsumer>(actor, RelocationSnapshotConsumer.class, consumer, applyRelocationSnapshotRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, applyRelocationSnapshotRepresentation1));
    }
  }
}
