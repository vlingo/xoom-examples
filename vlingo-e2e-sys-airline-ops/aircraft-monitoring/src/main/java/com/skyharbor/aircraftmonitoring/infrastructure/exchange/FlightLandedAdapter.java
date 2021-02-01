// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.rabbitmq.Message;

import static io.vlingo.lattice.exchange.MessageParameters.DeliveryMode.Durable;

public class FlightLandedAdapter implements ExchangeAdapter<FlightLanded, String, Message> {

  @Override
  public FlightLanded fromExchange(final Message exchangeMessage) {
    return new FlightLandedMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final FlightLanded flightLanded) {
    final String messagePayload = new FlightLandedMapper().localToExternal(flightLanded);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if (!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return SchemaReferences.LANDED.match(schemaName);
  }
}