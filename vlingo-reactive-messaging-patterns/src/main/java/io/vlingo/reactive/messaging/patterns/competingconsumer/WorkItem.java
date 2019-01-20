// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Address;

/**
 * WorkItem represent some work to be done.
 */
public class WorkItem {
  
  public final int id;
  protected Address consumedby;
  
  public WorkItem(final int id) {
    this.id = id;
  }
  
  public Address consumedBy() {
    return consumedby;
  }
  
  public void markConsumedBy(Actor actor) {
    this.consumedby = actor.address();
  }

  /* @see java.lang.Object#hashCode() */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  /* @see java.lang.Object#equals(java.lang.Object) */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WorkItem other = (WorkItem) obj;
    if (id != other.id)
      return false;
    return true;
  }

  /* @see java.lang.Object#toString() */
  @Override
  public String toString() {
    return new StringBuilder()
      .append("WorkItem(")
      .append("id=").append(id)
      .append(", consumedby=").append(consumedby)
      .append(")")
      .toString();
  }
}
