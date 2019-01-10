package io.vlingo.examples.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public interface OrderEvents {

    class OrderCreated {

        private List<OrderItem> orderItem;

        public OrderCreated(List<OrderItem> orderItems) {
            this.orderItem = new ArrayList<>(orderItems);
        }

    }
    class ItemChanged  {

        private ProductId productId;
        private int newQuantity;

        public ItemChanged(ProductId productId, int  newQuantity) {
            this.productId = productId;
            this.newQuantity = newQuantity;
        }

        public static ItemChanged with(ProductId productId, int  newQuantity) {
            return new ItemChanged(productId, newQuantity);
        }

    }
}
