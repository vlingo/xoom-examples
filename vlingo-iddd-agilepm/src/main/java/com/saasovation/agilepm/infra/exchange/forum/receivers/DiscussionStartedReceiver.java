package com.saasovation.agilepm.infra.exchange.forum.receivers;

import com.saasovation.agilepm.infra.exchange.forum.model.DiscussionStarted;
import com.saasovation.agilepm.model.product.Product;
import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.lattice.exchange.ExchangeReceiver;
import io.vlingo.lattice.grid.GridAddressFactory;

public class DiscussionStartedReceiver implements ExchangeReceiver<DiscussionStarted> {
    private final Stage stage;
    private final AddressFactory addressFactory;

    public DiscussionStartedReceiver(Stage stage) {
        this.stage = stage;
        this.addressFactory = new GridAddressFactory(IdentityGeneratorType.RANDOM);
    }

    @Override
    public void receive(DiscussionStarted message) {
        final String[] split = message.ownerId.split(":");
        final String productId = split[1];

        stage.actorOf(Product.class, addressFactory.from(productId))
                .andFinallyConsume(product -> {
                    product.attachDiscussion(message.discussionId);
                });
    }
}
