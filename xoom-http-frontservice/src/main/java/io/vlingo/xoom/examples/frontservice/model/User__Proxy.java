// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.model;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.SerializableConsumer;

public class User__Proxy implements User {

  private static final String attachPrivateToken1 = "attachPrivateToken(java.lang.String)";
  private static final String withContactRepresentation2 = "withContact(io.vlingo.xoom.http.sample.user.model.Contact)";
  private static final String withNameRepresentation3 = "withName(io.vlingo.xoom.http.sample.user.model.Name)";

  private final Actor actor;
  private final Mailbox mailbox;

  public User__Proxy(final Actor actor, final Mailbox mailbox){
    this.actor = actor;
    this.mailbox = mailbox;
  }

  @Override
  public void attachPrivateToken(String arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<User> consumer = (actor) -> actor.attachPrivateToken(arg0);
      mailbox.send(new LocalMessage<User>(actor, User.class, consumer, null, attachPrivateToken1));
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, attachPrivateToken1));
    }
  }
  public Completes<UserState> withContact(Contact arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<User> consumer = (actor) -> actor.withContact(arg0);
      final Completes<UserState> completes = Completes.using(actor.scheduler());
      mailbox.send(new LocalMessage<User>(actor, User.class, consumer, Returns.value(completes), withContactRepresentation2));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, withContactRepresentation2));
    }
    return null;
  }
  public Completes<UserState> withName(Name arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<User> consumer = (actor) -> actor.withName(arg0);
      final Completes<UserState> completes = Completes.using(actor.scheduler());
      mailbox.send(new LocalMessage<User>(actor, User.class, consumer, Returns.value(completes), withNameRepresentation3));
      return completes;
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, withNameRepresentation3));
    }
    return null;
  }
}
