package com.skyharbor.fleetcrew.infrastructure.exchange;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.rabbitmq.Message;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public class AircraftProducerAdapter implements ExchangeAdapter<IdentifiedDomainEvent, IdentifiedDomainEvent, Message> {

  private static final String SCHEMA_PREFIX = "SkyHarborPHX:groundops:com.skyharbor.fleetcrew";

  private final DomainEventMapper mapper = new DomainEventMapper();

  @Override
  public IdentifiedDomainEvent fromExchange(final Message exchangeMessage) {
    return mapper.externalToLocal(exchangeMessage);
  }

  @Override
  public Message toExchange(final IdentifiedDomainEvent event) {
    final Message message = mapper.localToExternal(event);
    message.messageParameters.typeName(resolveFullSchemaReference(event));
    return message;
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return schemaName.startsWith(SCHEMA_PREFIX);
  }

  private String resolveFullSchemaReference(final IdentifiedDomainEvent event) {
    final String semanticVersion = SemanticVersion.toString(event.sourceTypeVersion);
    return String.format("%s:%s:%s", SCHEMA_PREFIX, event.getClass().getSimpleName(), semanticVersion);
  }

}