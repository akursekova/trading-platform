package dev.akursekova.entities;

/*
* - id
- userId
- securityId
- type
- price
- quantity
- fullfilled
* */


public class Order {
    private long id;
    private long userId;
    private long securityId;
    private OrderType type;
    private int price;
    private int quantity;
    private Fulfilled fulfilled;

    public Order() {
        this.fulfilled = Fulfilled.NO;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public OrderType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(long securityId) {
        this.securityId = securityId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Fulfilled getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Fulfilled fulfilled) {
        this.fulfilled = fulfilled;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", orderType=").append(type);
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append(", fullfilled=").append(fulfilled);
        sb.append('}');
        return sb.toString();
    }
}
