package dev.akursekova.service;

import dev.akursekova.entities.Fulfilled;
import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.entities.Trade;
import dev.akursekova.repository.*;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class TradeService implements TradeServiceInterface {

    private static final Logger LOG = LogManager.getLogger(TradeService.class);

    private final TradeRepositoryInterface tradeRepository;
    private final OrderRepositoryInterface orderRepository;

    public TradeService(TradeRepositoryInterface tradeRepository,
                        OrderRepositoryInterface orderRepository) {
        this.tradeRepository = tradeRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    @SneakyThrows
    public Trade trade(Order order) {
        Optional<Order> possibleMatchingOrder = orderRepository.getAllOrders()
                .stream()
                .filter(order1 -> !order1.getType().equals(order.getType()))
                .filter(order1 -> order1.getSecurityId() == order.getSecurityId())
                .filter(order1 -> order1.getFulfilled().equals(Fulfilled.NO) || order1.getFulfilled().equals(Fulfilled.PARTIALLY))
                .filter(order1 -> priceCoefficient(order) * (order.getPrice() - order1.getPrice()) >= 0)
                .findFirst();

        if (possibleMatchingOrder.isPresent()) {
            Order matchingOrder = possibleMatchingOrder.get();

            long sellOrderId = order.getType().equals(OrderType.SELL) ? order.getId() : matchingOrder.getId();
            long buyOrderId = order.getType().equals(OrderType.BUY) ? order.getId() : matchingOrder.getId();
            int soldQuantity = Math.min(order.getQuantity(), matchingOrder.getQuantity());
            int sellPrice = Math.max(order.getPrice(), matchingOrder.getPrice());

            setFulfilled(order, matchingOrder);
            Trade trade = new Trade(sellOrderId, buyOrderId, sellPrice, soldQuantity);
            return tradeRepository.addTrade(trade);
        } else {
            return null;
        }
    }

    private void setFulfilled(Order order, Order matchingOrder) {

        if (order.getQuantity() > matchingOrder.getQuantity()) {
            order.setFulfilled(Fulfilled.PARTIALLY);
            matchingOrder.setFulfilled(Fulfilled.YES);
            order.setQuantity(order.getQuantity() - matchingOrder.getQuantity());
        } else if (order.getQuantity() < matchingOrder.getQuantity()) {
            order.setFulfilled(Fulfilled.YES);
            matchingOrder.setFulfilled(Fulfilled.PARTIALLY);
            matchingOrder.setQuantity(matchingOrder.getQuantity() - order.getQuantity());
        } else {
            order.setFulfilled(Fulfilled.YES);
            matchingOrder.setFulfilled(Fulfilled.YES);
        }

        LOG.debug("order with id = " + order.getId() + " has been updated: " + order.toString());
        LOG.debug("order with id = " + matchingOrder.getId() + " has been updated: " + matchingOrder.toString());
    }

    private int priceCoefficient(Order order) {
        if (order.getType().equals(OrderType.SELL)) {
            return -1;
        } else {
            return 1;
        }
    }
}
