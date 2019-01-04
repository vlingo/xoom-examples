package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;

import java.util.List;


public interface Cart {

    Completes<List<CartItem>> addItem(ProductId productId);

    Completes<List<CartItem>> removeItem(ProductId productId);

    Completes<List<CartItem>> removeAllItems();

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