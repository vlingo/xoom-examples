// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

public class DomainEventMapper implements ExchangeMapper<IdentifiedDomainEvent, Message> {

  @Override
  public Message localToExternal(final IdentifiedDomainEvent event) {
    final String messagePayload = JsonSerialization.serialized(event);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(MessageParameters.DeliveryMode.Durable));
  }

  @Override
  public IdentifiedDomainEvent externalToLocal(final Message message) {
    try {
      final String eventFullyQualifiedName = message.messageParameters.typeName();

      final Class<? extends IdentifiedDomainEvent> eventClass =
              (Class<? extends IdentifiedDomainEvent>) Class.forName(eventFullyQualifiedName);

      return JsonSerialization.deserialized(message.payloadAsText(), eventClass);
    } catch (final ClassNotFoundException e) {
      throw new IllegalArgumentException("Unable to handle message containing "
              + message.messageParameters.typeName(), e);
    }
  }
}