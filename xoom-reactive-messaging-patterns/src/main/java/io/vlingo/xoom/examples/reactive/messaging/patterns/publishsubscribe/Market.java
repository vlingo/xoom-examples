// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.publishsubscribe;

import io.vlingo.xoom.actors.pubsub.Topic;

public class Market extends Topic {

    public Market(final String name) {
        super(name);
    }

    @Override
    public boolean isSubTopic(final Topic topic) {
        return topic.name().startsWith(super.name());
    }

    @Override
    public String toString() {
        return super.name();
    }
}
