package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.repository.OrderRepositoryInterface;

import java.util.Optional;

public class TradingService {

    public static void trade(Order order, OrderRepository orderRepository){
        if (order.getType().equals(OrderType.BUY)){
            if (!orderRepository.orders.values().stream()
                    .filter(order1 -> order1.getType().equals(OrderType.SELL))
                    .filter(order1 -> order1.getPrice() <= order.getPrice())
                    .filter(order1 -> order1.getQuantity() >= order.getQuantity())
                    .findFirst().equals(Optional.empty())){

            }
        }
//        if (order.getType().equals(OrderType.SELL)){
//
//        }
    }

}
