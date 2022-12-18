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

public class TradeService {

    private static final Logger LOG = LogManager.getLogger(TradeService.class);

    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;

    public TradeService(TradeRepositoryInterface tradeRepository, OrderRepositoryInterface orderRepository) {
        this.tradeRepository = (TradeRepository) tradeRepository;
        this.orderRepository = (OrderRepository) orderRepository;
    }


    @SneakyThrows
    public void trade(Order order) {
        if (order.getType().equals(OrderType.BUY)) {
            Optional<Order> matchingSellOrder = orderRepository.getAllOrders().stream()
                    .filter(order1 -> order1.getType().equals(OrderType.SELL))
                    .filter(order1 -> order1.getFulfilled()
                            .equals(Fulfilled.NO) || order1.getFulfilled().equals(Fulfilled.PARTIALLY))
                    .filter(order1 -> order1.getPrice() <= order.getPrice())
                    .findFirst();
            if (matchingSellOrder.isPresent()) {
                Order sellOrder = matchingSellOrder.get();
                int soldQuantity = Math.min(order.getQuantity(), sellOrder.getQuantity());
                setFulfilled(sellOrder, order);
                Trade trade = new Trade(sellOrder.getId(), order.getId(), order.getPrice(), soldQuantity);
                tradeRepository.addTrade(trade);
            }
        } else {
            Optional<Order> matchingBuyOrder = orderRepository.getAllOrders().stream()
                    .filter(order1 -> order1.getType().equals(OrderType.BUY))
                    .filter(order1 -> order1.getFulfilled()
                            .equals(Fulfilled.NO) || order1.getFulfilled().equals(Fulfilled.PARTIALLY))
                    .filter(order1 -> order1.getPrice() >= order.getPrice())
                    .findFirst();
            if (matchingBuyOrder.isPresent()) {
                Order buyOrder = matchingBuyOrder.get();
                int soldQuantity = Math.min(order.getQuantity(), buyOrder.getQuantity());
                setFulfilled(order, buyOrder);
                Trade trade = new Trade(order.getId(), buyOrder.getId(), order.getPrice(), soldQuantity);
                tradeRepository.addTrade(trade);
            }
        }
    }

    private static void setFulfilled(Order sellOrder, Order buyOrder) {
        if (sellOrder.getQuantity() - buyOrder.getQuantity() > 0) { // sellOrder q = 100 buyOrder q = 90
            sellOrder.setFulfilled(Fulfilled.PARTIALLY);
            buyOrder.setFulfilled(Fulfilled.YES);
            sellOrder.setQuantity(sellOrder.getQuantity() - buyOrder.getQuantity());
            LOG.debug("sell Order has been updated: " + sellOrder.toString());
            LOG.debug("buy Order has been updated: " + buyOrder.toString());
        } else if (sellOrder.getQuantity() - buyOrder.getQuantity() < 0) { // sellOrder q = 90 buyOrder q = 100
            sellOrder.setFulfilled(Fulfilled.YES);
            buyOrder.setFulfilled(Fulfilled.PARTIALLY);
            buyOrder.setQuantity(buyOrder.getQuantity() - sellOrder.getQuantity());
            LOG.debug("sell Order has been updated: " + sellOrder.toString());
            LOG.debug("buy Order has been updated: " + buyOrder.toString());
        } else {
            sellOrder.setFulfilled(Fulfilled.YES);
            buyOrder.setFulfilled(Fulfilled.YES);
            LOG.debug("sell Order has been updated: " + sellOrder.toString());
            LOG.debug("buy Order has been updated: " + buyOrder.toString());
        }
    }

}
