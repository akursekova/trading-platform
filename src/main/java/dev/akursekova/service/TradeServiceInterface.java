package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.Trade;
import lombok.SneakyThrows;

public interface TradeServiceInterface {
    @SneakyThrows
    Trade trade(Order order);
}
