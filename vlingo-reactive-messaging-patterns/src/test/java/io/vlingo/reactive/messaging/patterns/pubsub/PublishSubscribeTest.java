// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pubsub;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.pubsub.DefaultPublisher;
import io.vlingo.actors.pubsub.Publisher;
import io.vlingo.actors.pubsub.Subscriber;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.publishsubscribe.*;
import org.junit.Test;

public class PublishSubscribeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testThatPublishSubscribeRuns() {

        System.out.println("Publish Subscribe: is starting.");

        final World world = World.startWithDefaults("publish-subscribe-test");

        final TestUntil until = TestUntil.happenings(6);

        final Subscriber<PriceQuoted> allMarketsSubscriber =
                world.actorFor(
                        Definition.has(AllMarketsSubscriber.class,
                                Definition.parameters(until)),
                        Subscriber.class);

        final Subscriber<PriceQuoted> nasdaqSubscriber =
                world.actorFor(
                        Definition.has(NASDAQSubscriber.class,
                                Definition.parameters(until)),
                        Subscriber.class);

        final Subscriber<PriceQuoted> nyseSubscriber =
                world.actorFor(
                        Definition.has(NYSESubscriber.class,
                                Definition.parameters(until)),
                        Subscriber.class);

        final Publisher publisher = new DefaultPublisher();

        publisher.subscribe(new Market("quotes"), allMarketsSubscriber);
        publisher.subscribe(new Market("quotes/NASDAQ"), nasdaqSubscriber);
        publisher.subscribe(new Market("quotes/NYSE"), nyseSubscriber);

        publisher.publish(new Market("quotes/NYSE"), new PriceQuoted("ORCL", new Money("121.13")));
        publisher.publish(new Market("quotes/NASDAQ"), new PriceQuoted( "MSFT", new Money("1099.76")));
        publisher.publish(new Market("quotes/DAX"), new PriceQuoted("SAP:GR", new Money("885.00")));
        publisher.publish(new Market("quotes/NKY"), new PriceQuoted("6701:JP", new Money("131.12")));

        until.happened();
    }

}
