package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.repository.OrderRepositoryInterface;
import dev.akursekova.repository.SecurityRepositoryInterface;
import dev.akursekova.repository.UserRepositoryInterface;

public interface OrderServiceInterface {

    void validateOrder(Order order) throws OrderCreationException;
}
