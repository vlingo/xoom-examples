// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import com.skyharbor.airtrafficcontrol.event.FlightTookOff;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.rabbitmq.Message;

import static io.vlingo.lattice.exchange.MessageParameters.DeliveryMode.Durable;

public class FlightTookOffAdapter implements ExchangeAdapter<FlightTookOff, String, Message> {

  @Override
  public FlightTookOff fromExchange(final Message exchangeMessage) {
    return new FlightTookOffMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final FlightTookOff flightTookOff) {
    final String messagePayload = new FlightTookOffMapper().localToExternal(flightTookOff);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if (!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return SchemaReferences.TOOK_OFF.match(schemaName);
  }
}