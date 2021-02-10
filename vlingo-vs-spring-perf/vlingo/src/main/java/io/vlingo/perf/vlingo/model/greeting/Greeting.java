// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.perf.vlingo.model.greeting;

import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.perf.vlingo.infrastructure.data.GreetingData;
import io.vlingo.perf.vlingo.infrastructure.data.UpdateGreetingData;

public interface Greeting {

    static Completes<GreetingState> defineGreeting(Stage stage, GreetingData data){
        Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
        Greeting greeting = stage.actorFor(Greeting.class, GreetingEntity.class);
        return greeting.defineGreeting(data);
    }


    Completes<GreetingState> defineGreeting(GreetingData data);

    Completes<GreetingState> updateMessage(UpdateGreetingData data);

    Completes<GreetingState> updateDescription(UpdateGreetingData data);
}