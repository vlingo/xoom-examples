package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;

import java.util.List;



public interface Cart {

    void addItem(ProductId productId);

    void removeItem(ProductId productId);

    void removeAllItems();

    Completes<List<CartItem>> queryCart();

    class CartItem {

        public final ProductId productId;
        public final Integer quantity;

        public CartItem(ProductId productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}