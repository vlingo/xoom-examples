// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

import java.util.Arrays;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.aggregator.RequestForQuotationProcessor.RequestForQuotation;
import io.vlingo.reactive.messaging.patterns.aggregator.RequestForQuotationProcessor.RetailItem;
import io.vlingo.reactive.messaging.patterns.aggregator.quotes.BudgetHikersPriceQuotesActor;
import io.vlingo.reactive.messaging.patterns.aggregator.quotes.HighSierraPriceQuotesActor;
import io.vlingo.reactive.messaging.patterns.aggregator.quotes.MountainAscentPriceQuotesActor;
import io.vlingo.reactive.messaging.patterns.aggregator.quotes.PinnacleGearPriceQuotesActor;
import io.vlingo.reactive.messaging.patterns.aggregator.quotes.RockBottomOuterwearPriceQuotesActor;

public class AggregatorTest {
  @Test
  public void testThatAggregatorRuns() {
    System.out.println("Aggregator: is starting.");

    final World world = World.startWithDefaults("aggregator-test");

    final TestUntil until = TestUntil.happenings(1);

    final Protocols protocols =
            world.actorFor(
                    Definition.has(
                            MountaineeringSuppliesOrderProcessor.class,
                            Definition.parameters(until)),
                    new Class[] { RequestForQuotationProcessor.class,
                                  RequestForQuotationSupplier.class,
                                  PriceQuotesFulfillmentWatcher.class });

    final Protocols.Three<RequestForQuotationProcessor, RequestForQuotationSupplier, PriceQuotesFulfillmentWatcher> three = Protocols.three(protocols);
    final RequestForQuotationProcessor processor = three._1;
    final RequestForQuotationSupplier supplier = three._2;

    world.actorFor(Definition.has(PriceQuoteAggregatorActor.class, Definition.parameters(three._3)), PriceQuoteAggregator.class);

    world.actorFor(Definition.has(BudgetHikersPriceQuotesActor.class, Definition.parameters(supplier)), PriceQuotes.class);
    world.actorFor(Definition.has(HighSierraPriceQuotesActor.class, Definition.parameters(supplier)), PriceQuotes.class);
    world.actorFor(Definition.has(MountainAscentPriceQuotesActor.class, Definition.parameters(supplier)), PriceQuotes.class);
    world.actorFor(Definition.has(PinnacleGearPriceQuotesActor.class, Definition.parameters(supplier)), PriceQuotes.class);
    world.actorFor(Definition.has(RockBottomOuterwearPriceQuotesActor.class,Definition.parameters(supplier)), PriceQuotes.class);

    processor.requestPriceQuotationFor(
            new RequestForQuotation(
                    "123",
                    Arrays.asList(
                            new RetailItem("1", 29.95),
                            new RetailItem("2", 99.95),
                            new RetailItem("3", 14.95))));

    processor.requestPriceQuotationFor(
            new RequestForQuotation(
                    "125",
                    Arrays.asList(
                            new RetailItem("4", 39.99),
                            new RetailItem("5", 199.95),
                            new RetailItem("6", 149.95),
                            new RetailItem("7", 724.99))));

    processor.requestPriceQuotationFor(
            new RequestForQuotation(
                    "129",
                    Arrays.asList(
                            new RetailItem("8", 119.99),
                            new RetailItem("9", 499.95),
                            new RetailItem("10", 519.00),
                            new RetailItem("11", 209.50))));

    processor.requestPriceQuotationFor(
            new RequestForQuotation(
                    "135",
                    Arrays.asList(
                            new RetailItem("12", 0.97),
                            new RetailItem("13", 9.50),
                            new RetailItem("14", 1.99))));

    processor.requestPriceQuotationFor(
            new RequestForQuotation(
                    "140",
                    Arrays.asList(
                            new RetailItem("15", 107.50),
                            new RetailItem("16", 9.50),
                            new RetailItem("17", 599.99),
                            new RetailItem("18", 249.95),
                            new RetailItem("19", 789.99))));

    until.completes();

    System.out.println("Aggregator: is completed.");
  }
}
