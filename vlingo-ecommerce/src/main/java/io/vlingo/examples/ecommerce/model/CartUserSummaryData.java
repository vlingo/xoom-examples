package io.vlingo.examples.ecommerce.model;

public class CartUserSummaryData {

    public final String userId;
    public final String cartId;
    public final String amount;

    public CartUserSummaryData(String userId, String cartId, String amount) {
        this.userId = userId;
        this.cartId = cartId;
        this.amount = amount;
    }

    CartUserSummaryData(int cartId, int userId, float amount) {
        this.cartId = Integer.toString(cartId);
        this.userId = Integer.toString(userId);
        this.amount = Float.toString(amount);
    }

    public static CartUserSummaryData empty() {
        // Should be emppty?
        return new CartUserSummaryData(null, null, null);
    }
}
