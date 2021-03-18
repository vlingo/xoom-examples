// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.model.controller.ControllerAuthorized;
import com.skyharbor.airtrafficcontrol.model.flight.*;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.dispatch.ConfirmDispatchedResultInterest;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<String>>>, ConfirmDispatchedResultInterest {
  private static final Logger logger = LoggerFactory.getLogger(ExchangeDispatcher.class);

  private DispatcherControl control;
  private final List<Exchange> producerExchanges;
  private final Map<String, Set<String>> eventsByExchangeName = new HashMap<>();

  public ExchangeDispatcher(final Exchange ...producerExchanges) {
    this.eventsByExchangeName.put("air-traffic-control", new HashSet<>());
    this.eventsByExchangeName.get("air-traffic-control").add(FlightTookOff.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(FlightClearedForLanding.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(FlightLanded.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(OutboundTaxingInitiated.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(ControllerAuthorized.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(FlightClearedForTakeOff.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(EnteredFlightLine.class.getCanonicalName());
    this.eventsByExchangeName.get("air-traffic-control").add(FlightDepartedGate.class.getCanonicalName());
    this.producerExchanges = Arrays.asList(producerExchanges);
  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<String>> dispatchable) {
    logger.debug("Going to dispatch id {}", dispatchable.id());

    for (Entry<String> entry : dispatchable.entries()) {
      this.send(JsonSerialization.deserialized(entry.entryData(), entry.typed()));
    }

    this.control.confirmDispatched(dispatchable.id(), this);
  }

  @Override
  public void confirmDispatchedResultedIn(Result result, String dispatchId) {
      logger.debug("Dispatch id {} resulted in {}", dispatchId, result);
  }

  @Override
  public void controlWith(DispatcherControl control) {
    this.control = control;
  }

  private void send(final Object event) {
    this.findInterestedIn(event).forEach(exchange -> exchange.send(event));
  }

  private Stream<Exchange> findInterestedIn(final Object event) {
    final Set<String> exchangeNames =
          eventsByExchangeName.entrySet().stream().filter(exchange -> {
             final Set<String> events = exchange.getValue();
             return events.contains(event.getClass().getCanonicalName());
         }).map(Map.Entry::getKey).collect(Collectors.toSet());

    return this.producerExchanges.stream().filter(exchange -> exchangeNames.contains(exchange.name()));
  }

}