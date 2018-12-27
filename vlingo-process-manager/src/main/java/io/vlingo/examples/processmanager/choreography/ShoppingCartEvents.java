package io.vlingo.examples.processmanager.choreography;

import io.vlingo.lattice.model.DomainEvent;

public class ShoppingCartEvents {

    public static class CreatedEvent extends DomainEvent {

        public CreatedEvent(String shoppingCartId, UserId userId) {
            super(1);
            this.shoppingCartId = shoppingCartId;
            this.userId = userId;
        }

        public final String shoppingCartId;
        public final UserId userId;

        public static CreatedEvent forUser(String shoppingCartId, UserId userId) {
            return new CreatedEvent(shoppingCartId, userId);
        }
    }


    public static class ProductAddedEvent extends DomainEvent {
        public ProductAddedEvent(UserId userId, ProductId productId) {
            super(1);
            this.userId = userId;
            this.productId = productId;
        }

        public static ProductAddedEvent with(UserId userId, ProductId productId) {
            return new ProductAddedEvent(userId, productId);
        }

        public final ProductId productId;
        public final UserId userId;
    }


    public static class ProductRemovedEvent extends DomainEvent {
        public final ProductId productId;
        public final UserId userId;

        public static ProductRemovedEvent with(UserId userId, ProductId productId) {
            return new ProductRemovedEvent(userId, productId);
        }

        public ProductRemovedEvent(UserId userId, ProductId productId) {
            super(1);
            this.productId = productId;
            this.userId = userId;
        }
    }

    public static class AllItemsRemovedEvent extends DomainEvent {
        public final UserId userId;

        public AllItemsRemovedEvent(UserId userId) {
            super(1);
            this.userId = userId;
        }

        public static AllItemsRemovedEvent with(UserId userId) {
            return new AllItemsRemovedEvent(userId);
        }
    }
}
