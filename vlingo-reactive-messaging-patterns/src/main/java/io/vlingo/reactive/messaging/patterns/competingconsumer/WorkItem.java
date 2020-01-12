// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;
/**
 * WorkItem represent some work to be done.
 */
public class WorkItem {
  
  private final int id;
  
  public WorkItem(final int id) {
    this.id = id;
  }
  
  public int id() {
    return id;
  }

  /* @see java.lang.Object#toString() */
  @Override
  public String toString() {
    return "WorkItem[id=" + id + "]";
  }
}
