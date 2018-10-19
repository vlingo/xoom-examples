package io.vlingo.router.contentbased.order;

/**
 * @author Chandrabhan Kumhar
 * Stores details for a specific order
 */
public class OrderItem {
    private String id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "[" + String.join(", ", this.id, this.itemType, this.description, this.price.toString()) + "]" ;
    }

}
