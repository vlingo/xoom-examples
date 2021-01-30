package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.inventory.event.AircraftConsigned;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.lattice.exchange.rabbitmq.Message;

public class AircraftConsignedAdapter implements ExchangeAdapter<AircraftConsigned, String, Message> {

  private final String supportedSchemaName;

  public AircraftConsignedAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public AircraftConsigned fromExchange(final Message exchangeMessage) {
    return new AircraftConsignedMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final AircraftConsigned local) {
    final String messagePayload = new AircraftConsignedMapper().localToExternal(local);
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