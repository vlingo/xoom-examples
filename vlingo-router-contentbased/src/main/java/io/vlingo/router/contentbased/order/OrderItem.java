package io.vlingo.router.contentbased.order;

/**
 * @author Chandrabhan Kumhar
 * Stores details for a specific order
 */
public class OrderItem {
    public final String id;
    private String itemType;
    private String description;
    private Double price;

    public OrderItem(String id, String itemType, String description, Double price) {
        this.id = id;
        this.itemType = itemType;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getItemType() {
        return itemType;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return "[" + String.join(", ", this.id, this.itemType, this.description, this.price.toString()) + "]" ;
    }

}
