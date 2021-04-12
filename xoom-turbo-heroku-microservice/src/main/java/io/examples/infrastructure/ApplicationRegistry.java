// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.infrastructure;

import io.micronaut.context.ApplicationContext;
import io.vlingo.actors.World;
import io.vlingo.xoom.VlingoServer;

import javax.inject.Singleton;

@Singleton
public class ApplicationRegistry {

    private final ApplicationContext applicationContext;

    public ApplicationRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public World retrieveWorld() {
        return applicationContext.getBean(VlingoServer.class).getVlingoScene().getWorld();
    }
}
