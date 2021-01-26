package com.skyharbor.fleetcrew.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.lattice.exchange.rabbitmq.Message;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;

public class AircraftConsumerAdapter implements ExchangeAdapter<AircraftData, String, Message> {

  private final String supportedSchemaName;

  public AircraftConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public AircraftData fromExchange(final Message exchangeMessage) {
    return new AircraftDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final AircraftData local) {
    final String messagePayload = new AircraftDataMapper().localToExternal(local);
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