package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;

public interface OrderRepositoryInterface {

    void addOrder(Order order) throws OrderCreationException;

    Order getOrder(Long id) throws OrderNotExistException;

//    void showOrders();
}
