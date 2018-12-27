package io.vlingo.examples.processmanager.choreography;

import java.util.List;



public interface ShoppingCart {

    void addItem(ProductId productId);

    void removeItem(ProductId productId);

    void removeAllItems();

    List<CartItem> queryCart();

    class CartItem {

        public final ProductId productId;
        public final Integer quantity;

        public CartItem(ProductId productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}