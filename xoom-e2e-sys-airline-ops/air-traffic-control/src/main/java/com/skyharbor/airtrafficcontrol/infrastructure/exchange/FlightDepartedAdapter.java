// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.vgoairlines.airportterminal.event.FlightDeparted;
import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

public class FlightDepartedAdapter implements ExchangeAdapter<FlightDeparted, String, Message> {

  @Override
  public FlightDeparted fromExchange(final Message exchangeMessage) {
    return new FlightDepartedMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final FlightDeparted local) {
    final String messagePayload = new FlightDepartedMapper().localToExternal(local);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return SchemaReferences.FLIGHT_DEPARTED.match(schemaName);
  }

}