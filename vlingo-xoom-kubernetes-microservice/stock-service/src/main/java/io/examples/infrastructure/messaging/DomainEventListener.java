package io.examples.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.examples.infrastructure.ApplicationRegistry;
import io.examples.stock.domain.ItemId;
import io.examples.stock.domain.Location;
import io.examples.stock.domain.Stock;
import io.micronaut.configuration.rabbitmq.annotation.Queue;
import io.micronaut.configuration.rabbitmq.annotation.RabbitListener;
import io.vlingo.actors.World;

import java.io.IOException;

/**
 * The {@code DomainEventListener} receives <code>DomainEvent</code>
 * through JMS channel / queue from other Bounded Contexts.
 *
 * @author Danilo Ambrosio
 */
@RabbitListener
public class DomainEventListener {

    private final ApplicationRegistry applicationRegistry;
    private final ObjectMapper objectMapper;

    public DomainEventListener(final ApplicationRegistry applicationRegistry,
                               final ObjectMapper objectMapper) {
        this.applicationRegistry = applicationRegistry;
        this.objectMapper = objectMapper;
    }

    @Queue("registered-order")
    public void handle(final byte[] data) throws IOException {
        final World world = applicationRegistry.retrieveWorld();
        final JsonNode json = objectMapper.readTree(data);
        final Location location = Location.valueOf(json.get("site").asText());
        final ItemId itemId = ItemId.of(json.get("productId").get("id").asLong());
        final Integer quantity = json.get("quantity").asInt();

        Stock.unload(world.stage(), location, itemId, quantity);
    }

}
