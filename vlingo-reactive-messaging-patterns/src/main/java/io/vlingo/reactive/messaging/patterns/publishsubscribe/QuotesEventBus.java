// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

import io.vlingo.actors.event.SubChannelClassification;
import io.vlingo.common.SubClassification;

public class QuotesEventBus extends SubChannelClassification<PriceQuoted, QuotationProcessor, Market> {

    public QuotesEventBus() {

        super(new SubClassification<Market>() {

            @Override
            public boolean isEqual(final Market subscribedToClassifier, final Market eventClassifier) {
                return subscribedToClassifier.equals(eventClassifier);
            }

            @Override
            public boolean isSubclass(final Market subscribedToClassifier, final Market eventClassifier) {
                return subscribedToClassifier.name().startsWith(eventClassifier.name());
            }

        });
    }

    @Override
    protected Market classify(final PriceQuoted event) {
        return event.market();
    }

    @Override
    protected void publish(final PriceQuoted event, final QuotationProcessor quotationProcessor) {
        quotationProcessor.process(event);
    }
}