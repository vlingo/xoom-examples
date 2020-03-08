package io.vlingo.examples.ecommerce.model;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public class CartEvents {

    public static class CreatedForUser extends IdentifiedDomainEvent {
        public final String cartId;
        public final UserId userId;

        public CreatedForUser(String cartId, UserId userId) {
            super(1);
            this.cartId = cartId;
            this.userId = userId;
        }

        public static CreatedForUser forUser(String shoppingCartId, UserId userId) {
            return new CreatedForUser(shoppingCartId, userId);
        }

		@Override
		public String identity() {
			return userId.getId();
		}
    }


    public static class ProductQuantityChangeEvent extends IdentifiedDomainEvent {
        public final String cartId;
        public final ProductId productId;
        public final int quantityChange;
        public final int newQuantity;
        public final UserId userId;

        public ProductQuantityChangeEvent(String cartId, UserId userId, ProductId productId, int quantityChange,
                                          int newQuantity) {
            super(1);
            this.cartId = cartId;
            this.userId = userId;
            this.productId = productId;
            this.quantityChange = quantityChange;
            this.newQuantity = newQuantity;
        }

        public static ProductQuantityChangeEvent with(String cartId, UserId userId, ProductId productId,
                                                      int quantityChange, int newQuantity) {
            return new ProductQuantityChangeEvent(cartId, userId, productId, quantityChange, newQuantity);
        }

		@Override
		public String identity() {
			return userId.getId();
		}
    }

    public static class AllItemsRemovedEvent extends IdentifiedDomainEvent {
        public final String cartId;
        public final UserId userId;


        public AllItemsRemovedEvent(String cartId, UserId userId) {
            super(1);
            this.cartId = cartId;
            this.userId = userId;
        }

        public static AllItemsRemovedEvent with(String cartId, UserId userId) {
            return new AllItemsRemovedEvent(cartId, userId);
        }

		@Override
		public String identity() {
			return userId.getId();
		}
    }
}
