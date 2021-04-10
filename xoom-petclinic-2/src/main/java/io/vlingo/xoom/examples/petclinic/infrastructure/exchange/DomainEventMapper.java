package io.vlingo.xoom.examples.petclinic.infrastructure.exchange;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
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
