// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.model.greeting;

import io.vlingo.xoom.examples.perf.vlingo.infrastructure.data.GreetingData;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.data.UpdateGreetingData;
import io.vlingo.xoom.symbio.store.object.StateObject;

public final class GreetingState extends StateObject {
  public final String id;
  public final String message;
  public final String description;
  public final int messageCounter;
  public final int descriptionCounter;

  public static GreetingState empty() {return new GreetingState("","",0,"",0);}

  public static GreetingState identifiedBy(final String id) {
    return new GreetingState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && message == null && description == null;
  }


  public static GreetingState withGreetingData(final String id, final GreetingData value) {
    return new GreetingState(id, value.message, 0, value.description, 0);
  }

  public GreetingState updateMessage(final UpdateGreetingData data) {
    return new GreetingState(this.id, data.value, this.messageCounter + 1, this.description, this.descriptionCounter);
  }

  public GreetingState updateDescription(final UpdateGreetingData data) {
    return new GreetingState(this.id, this.message, this.messageCounter, data.value, this.descriptionCounter + 1);
  }

  GreetingState(final long persistenceId, final long version, final String id, final String message, final int messageCounter, final String description,
                final int descriptionCounter) {
    super(persistenceId, version);
    this.id = id;
    this.message = message;
    this.messageCounter = messageCounter;
    this.description = description;
    this.descriptionCounter = descriptionCounter;
  }

  GreetingState(final String id, final String message, final int messageCounter, final String description, final int descriptionCounter) {
    this(Unidentified, 0, id, message, messageCounter, description, descriptionCounter);
  }

  private GreetingState(final String id) {
    this(Unidentified, 0, id, null,0,null,0);
  }

  private GreetingState() {
    this(Unidentified, 0, null, null,0,null,0);
  }
}
