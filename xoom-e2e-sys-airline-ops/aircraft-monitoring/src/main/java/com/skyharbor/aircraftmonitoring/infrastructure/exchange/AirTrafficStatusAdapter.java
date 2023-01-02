// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.aircraftmonitoring.infrastructure.exchange.AirTrafficDataMapper.AirTrafficData;
import com.skyharbor.aircraftmonitoring.model.flight.Status;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

import static io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode.Durable;

public class AirTrafficStatusAdapter implements ExchangeAdapter<Tuple2<Status, AirTrafficData>, String, Message> {

  private static final Tuple2<Status, AirTrafficData> EMPTY = Tuple2.from(null, AirTrafficData.empty());

  @Override
  public Tuple2<Status, AirTrafficData> fromExchange(final Message message) {
    final Status status = findRelatedStatus(message);

    final AirTrafficData data =
            new AirTrafficDataMapper().externalToLocal(message.payloadAsText());

    return Tuple2.from(status, data);
  }

  @Override
  public Message toExchange(final Tuple2<Status, AirTrafficData> tuple) {
    final String messagePayload = new AirTrafficDataMapper().localToExternal(tuple._2);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if (!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return SchemaReference.matchAny(schemaName);
  }

  private Status findRelatedStatus(final Message message) {
    final SchemaReference schemaReference =
            SchemaReference.find(message.messageParameters.typeName());

    switch (schemaReference) {
      case DEPARTED_FROM_GATE:
        return Status.DEPARTED_GATE;
      case TOOK_OFF:
        return Status.IN_FLIGHT;
      case LANDED:
        return Status.LANDED;
      default:
        throw new UnsupportedOperationException("Unable to find the related status for " + schemaReference.name());
    }

  }

  public static Class<Tuple2<Status, AirTrafficData>> supportedLocalClass() {
    return (Class<Tuple2<Status, AirTrafficData>>) EMPTY.getClass();
  }

}
