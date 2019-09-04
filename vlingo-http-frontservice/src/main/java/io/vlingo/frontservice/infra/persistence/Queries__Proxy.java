package io.vlingo.frontservice.infra.persistence;

import java.util.Collection;
import java.util.function.Consumer;

import io.vlingo.actors.Actor;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.actors.Returns;
import io.vlingo.common.BasicCompletes;
import io.vlingo.common.Completes;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;

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
      final Consumer<Queries> consumer = (actor) -> actor.profileOf(arg0);
      final Completes<io.vlingo.frontservice.data.ProfileData> completes = new BasicCompletes<>(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), profileOfRepresentation1));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, profileOfRepresentation1));
    }
    return null;
  }
  public Completes<UserData> userDataOf(java.lang.String arg0) {
    if (!actor.isStopped()) {
      final Consumer<Queries> consumer = (actor) -> actor.userDataOf(arg0);
      final Completes<io.vlingo.frontservice.data.UserData> completes = new BasicCompletes<>(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), userDataOfRepresentation2));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, userDataOfRepresentation2));
    }
    return null;
  }
  public Completes<Collection<UserData>> usersData() {
    if (!actor.isStopped()) {
      final Consumer<Queries> consumer = (actor) -> actor.usersData();
      final Completes<Collection<UserData>> completes = new BasicCompletes<>(actor.scheduler());
      mailbox.send(new LocalMessage<Queries>(actor, Queries.class, consumer, Returns.value(completes), usersDataRepresentation3));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, usersDataRepresentation3));
    }
    return null;
  }
}
