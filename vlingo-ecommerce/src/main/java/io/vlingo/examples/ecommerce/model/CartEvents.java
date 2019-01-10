package io.vlingo.examples.ecommerce.model;

import io.vlingo.lattice.model.DomainEvent;

public class CartEvents {

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


    public static class ProductQuantityChangeEvent extends DomainEvent {
        public ProductQuantityChangeEvent(UserId userId, ProductId productId, int quantityChange, int newQuantity) {
            super(1);
            this.userId = userId;
            this.productId = productId;
            this.quantityChange = quantityChange;
            this.newQuantity = newQuantity;
        }

        public static ProductQuantityChangeEvent with(UserId userId, ProductId productId, int quantityChange, int newQuantity) {
            return new ProductQuantityChangeEvent(userId, productId, quantityChange, newQuantity);
        }

        public final ProductId productId;
        public final int quantityChange;
        public final int newQuantity;
        public final UserId userId;
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
