// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.lattice.exchange.rabbitmq.Message;

public class ControllerConsumerAdapter implements ExchangeAdapter<ControllerData, String, Message> {

  private final String supportedSchemaName;

  public ControllerConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public ControllerData fromExchange(final Message exchangeMessage) {
    return new ControllerDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final ControllerData local) {
    final String messagePayload = new ControllerDataMapper().localToExternal(local);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return supportedSchemaName.equalsIgnoreCase(schemaName);
  }

}