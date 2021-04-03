package io.vlingo.cars.model;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition.SerializationProxy;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.actors.Returns;
import io.vlingo.common.Completes;
import io.vlingo.common.SerializableConsumer;
import java.lang.String;

public class Car__Proxy extends ActorProxyBase<io.vlingo.cars.model.Car> implements io.vlingo.cars.model.Car {

  private static final String defineWithRepresentation1 = "defineWith(java.lang.String, java.lang.String, java.lang.String)";
  private static final String registerWithRepresentation2 = "registerWith(java.lang.String)";

  private final Actor actor;
  private final Mailbox mailbox;

  public Car__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.cars.model.Car.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Car__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  public io.vlingo.common.Completes<io.vlingo.cars.model.CarState> defineWith(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
    if (!actor.isStopped()) {
      ActorProxyBase<Car> self = this;
      final SerializableConsumer<Car> consumer = (actor) -> actor.defineWith(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1), ActorProxyBase.thunk(self, (Actor)actor, arg2));
      final io.vlingo.common.Completes<io.vlingo.cars.model.CarState> returnValue = Completes.using(actor.scheduler());
      if (mailbox.isPreallocated()) { mailbox.send(actor, Car.class, consumer, Returns.value(returnValue), defineWithRepresentation1); }
      else { mailbox.send(new LocalMessage<Car>(actor, Car.class, consumer, Returns.value(returnValue), defineWithRepresentation1)); }
      return returnValue;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, defineWithRepresentation1));
    }
    return null;
  }
  public io.vlingo.common.Completes<io.vlingo.cars.model.CarState> registerWith(java.lang.String arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<Car> self = this;
      final SerializableConsumer<Car> consumer = (actor) -> actor.registerWith(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      final io.vlingo.common.Completes<io.vlingo.cars.model.CarState> returnValue = Completes.using(actor.scheduler());
      if (mailbox.isPreallocated()) { mailbox.send(actor, Car.class, consumer, Returns.value(returnValue), registerWithRepresentation2); }
      else { mailbox.send(new LocalMessage<Car>(actor, Car.class, consumer, Returns.value(returnValue), registerWithRepresentation2)); }
      return returnValue;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, registerWithRepresentation2));
    }
    return null;
  }
}
