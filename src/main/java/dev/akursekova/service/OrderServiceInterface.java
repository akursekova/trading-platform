package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;

public interface OrderServiceInterface {

    void validateAndCreateOrder(Order order) throws OrderCreationException;
}
