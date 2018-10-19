package io.vlingo.router.contentbased.order;

/**
 * @author Chandrabhan Kumhar
 * Store details of the order placed
 */
public class OrderPlaced {

    private Order order;

    public OrderPlaced(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
