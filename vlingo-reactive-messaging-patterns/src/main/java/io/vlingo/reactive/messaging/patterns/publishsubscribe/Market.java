// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

public class Market {

    private final String name;

    public Market(final String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean equals(final Object other) {

        if(other == null || !other.getClass().equals(getClass())) {
            return false;
        }

        final Market otherMarket = (Market) other;

        if(!otherMarket.name().equals(this.name)) {
            return false;
        }

        return true;
    }
}
