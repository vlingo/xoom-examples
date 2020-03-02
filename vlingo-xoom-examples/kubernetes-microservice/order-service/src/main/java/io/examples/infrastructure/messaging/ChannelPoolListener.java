package io.examples.infrastructure.messaging;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.configuration.rabbitmq.connect.ChannelInitializer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * The {@code ChannelPoolListener} creates and configures a JMS exchange / queue
 * to send {@link io.examples.order.domain.DomainEvent} to other Bounded Contexts.
 *
 * @author Danilo Ambrosio
 */
@Singleton
public class ChannelPoolListener extends ChannelInitializer {

    @Inject
    private ObjectMapper mapper;

    @Override
    public void initialize(final Channel channel) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        channel.exchangeDeclare("order", BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare("registered-order", true, false, false, null);
        channel.queueBind("registered-order", "order", "registered-order");
    }

}
