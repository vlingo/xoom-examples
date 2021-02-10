// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.perf.spring.services;

import io.vlingo.perf.spring.dtos.GreetingUpdateRequest;
import io.vlingo.perf.spring.entities.Greeting;

public interface GreetingService {

	Greeting changeMessage(String greetingId, GreetingUpdateRequest request);

	Greeting changeDescription(String greetingId,GreetingUpdateRequest request);
}
