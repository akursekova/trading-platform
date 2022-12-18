package dev.akursekova.entities;


import lombok.Data;

@Data
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
}
