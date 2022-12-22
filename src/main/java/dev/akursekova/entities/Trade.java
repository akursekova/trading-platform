package dev.akursekova.entities;

import lombok.Data;

@Data
public class Trade {
    private long id;
    private long sellOrderId;
    private long buyOrderId;
    private int price;
    private int quantity;

    public Trade(long sellOrderId, long buyOrderId, int price, int quantity) {
        this.sellOrderId = sellOrderId;
        this.buyOrderId = buyOrderId;
        this.price = price;
        this.quantity = quantity;
    }
}
