package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.rabbitmq.Message;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

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