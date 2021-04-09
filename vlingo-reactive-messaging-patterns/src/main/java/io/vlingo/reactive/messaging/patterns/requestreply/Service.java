// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.requestreply;

import io.vlingo.common.Completes;

public interface Service {
  void requestFor(final String what, final Consumer consumer);
  Completes<String> query(final String what);
}
