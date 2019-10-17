package io.vlingo.examples.ecommerce.model;

public class CartUserSummaryData {

    public final String userId;
    public final String cartId;
    public final String numberOfItems;

    public CartUserSummaryData(String userId, String cartId, String numberOfItems) {
        this.userId = userId;
        this.cartId = cartId;
        this.numberOfItems = numberOfItems;
    }

    public static CartUserSummaryData empty() {
        return new CartUserSummaryData(null, null, null);
    }

    public static  CartUserSummaryData from(String userId, String cartId, String numberOfItems) {
        return new CartUserSummaryData(userId, cartId, numberOfItems);
    }
}
