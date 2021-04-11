// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.GreetingState;

import java.util.Collection;

public interface Queries {
    Completes<GreetingState> greetingWithId(final String id);
    Completes<Collection<GreetingState>> greetings();
}
