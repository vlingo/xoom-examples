package io.examples.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.examples.stock.application.StockApplicationServices;
import io.examples.stock.application.UnloadItem;
import io.micronaut.configuration.rabbitmq.annotation.Queue;
import io.micronaut.configuration.rabbitmq.annotation.RabbitListener;
import io.vlingo.common.Completes;

import java.io.IOException;

/**
 * The {@code DomainEventListener} receives <code>DomainEvent</code>
 * through JMS channel / queue from other Bounded Contexts.
 *
 * @author Danilo Ambrosio
 */
@RabbitListener
public class DomainEventListener {

    private final StockApplicationServices stockApplicationServices;
    private final ObjectMapper objectMapper;

    public DomainEventListener(final StockApplicationServices stockApplicationServices,
                               final ObjectMapper objectMapper) {
        this.stockApplicationServices = stockApplicationServices;
        this.objectMapper = objectMapper;
    }

    @Queue("registered-order")
    public void handle(final byte[] data) throws IOException {
        final JsonNode json = objectMapper.readTree(data);
        final String location = json.get("site").asText();
        final Long itemId = json.get("productId").get("id").asLong();
        final Integer quantity = json.get("quantity").asInt();

        Completes.withSuccess(stockApplicationServices)
                .andThenConsume(service -> service.unloadItem(new UnloadItem(location, itemId, quantity)));
    }

}
