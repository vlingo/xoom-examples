package io.vlingo.xoom.examples.frontservice.infra.persistence;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.SerializableConsumer;
import io.vlingo.xoom.examples.frontservice.data.ProfileData;
import io.vlingo.xoom.examples.frontservice.data.UserData;

import java.util.Collection;

public class Queries__Proxy implements Queries {

  private static final String profileOfRepresentation1 = "profileOf(java.lang.String)";
  private static final String userDataOfRepresentation2 = "userDataOf(java.lang.String)";
  private static final String usersDataRepresentation3 = "usersData()";

  private final Actor actor;
  private final Mailbox mailbox;

  public Queries__Proxy(final Actor actor, final Mailbox mailbox){
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public Completes<ProfileData> profileOf(java.lang.String arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<Queries> consumer = (actor) -> actor.profileOf(arg0);
      final Completes<io.vlingo.xoom.examples.frontservice.data.ProfileData> completes = Completes.using(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), profileOfRepresentation1));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, profileOfRepresentation1));
    }
    return null;
  }
  public Completes<UserData> userDataOf(java.lang.String arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<Queries> consumer = (actor) -> actor.userDataOf(arg0);
      final Completes<io.vlingo.xoom.examples.frontservice.data.UserData> completes = Completes.using(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), userDataOfRepresentation2));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, userDataOfRepresentation2));
    }
    return null;
  }
  public Completes<Collection<UserData>> usersData() {
    if (!actor.isStopped()) {
      final SerializableConsumer<Queries> consumer = (actor) -> actor.usersData();
      final Completes<Collection<UserData>> completes = Completes.using(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), usersDataRepresentation3));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, usersDataRepresentation3));
    }
    return null;
  }
}
