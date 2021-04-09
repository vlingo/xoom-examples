package io.vlingo.xoom.examples.ecommerce.model;

public class CartUserSummaryData {

    public final String userId;
    public final String cartId;
    public final int numberOfItems;

    public CartUserSummaryData(String userId, String cartId, int numberOfItems) {
        this.userId = userId;
        this.cartId = cartId;
        this.numberOfItems = numberOfItems;
    }

    public static CartUserSummaryData empty() {
        return new CartUserSummaryData(null, null, 0);
    }

    public static CartUserSummaryData identitiedBy(String userId) {
        return new CartUserSummaryData(userId, null, 0);
    }

    public static  CartUserSummaryData from(String userId, String cartId, int numberOfItems) {
        return new CartUserSummaryData(userId, cartId, numberOfItems);
    }

    public CartUserSummaryData mergeWith(String userId, String cartId, int addedNumberOfItems) {
        if (userId.equals(this.userId) && cartId.equals(this.cartId)) {
            return new CartUserSummaryData(this.userId, this.cartId, this.numberOfItems + addedNumberOfItems);
        } else {
            return this;
        }
    }

	@Override
	public String toString() {
		return "CartUserSummaryData [userId=" + userId + ", cartId=" + cartId + ", numberOfItems=" + numberOfItems + "]";
	}
}
