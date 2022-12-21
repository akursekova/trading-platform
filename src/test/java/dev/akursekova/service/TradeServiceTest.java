package dev.akursekova.service;

import dev.akursekova.entities.*;
import dev.akursekova.repository.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    private TradeServiceInterface tradeService;
    private OrderRepositoryInterface orderRepository;
    private Map<Long, Order> orders;
    private TradeRepositoryInterface tradeRepository;
    private Map<Long, Trade> trades;

    private UserRepositoryInterface userRepository;
    private Map<Long, User> users;
    private SecurityRepositoryInterface securityRepository;
    private Map<Long, Security> securities;

    @SneakyThrows
    @BeforeEach
    void setup() {
        orders = new HashMap<>();
        orderRepository = new OrderRepository(orders);

        trades = new HashMap<>();
        tradeRepository = new TradeRepository(trades);

        users = new HashMap<>();
        userRepository = new UserRepository(users);

        securities = new HashMap<>();
        securityRepository = new SecurityRepository(securities);

        this.tradeService = new TradeService(tradeRepository, orderRepository);

        Order sellOrder = new Order();
        sellOrder.setId(1L);
        sellOrder.setUserId(1L);
        sellOrder.setSecurityId(1L);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setQuantity(50);
        sellOrder.setPrice(50);
        orderRepository.addOrder(sellOrder);

        Order buyOrder = new Order();
        buyOrder.setId(1L);
        buyOrder.setUserId(2L);
        buyOrder.setSecurityId(1L);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setQuantity(50);
        buyOrder.setPrice(50);
        orderRepository.addOrder(buyOrder);
    }

    @SneakyThrows
    @Test
    void test_trade_GivenBuyOrder_MatchingSellOrderFound_BuyOrderPartiallyFulfilled_TradeSuccessfullyCreated() {
        Order buyOrder = new Order();
        buyOrder.setId(2L);
        buyOrder.setUserId(3L);
        buyOrder.setSecurityId(1L);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setQuantity(100);
        buyOrder.setPrice(60);
        orderRepository.addOrder(buyOrder);

        tradeService.trade(buyOrder);
        assertEquals(1, tradeRepository.getAllTrades().size());
        assertEquals(Fulfilled.PARTIALLY, buyOrder.getFulfilled());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenBuyOrder_MatchingSellOrderFound_BuyOrderFulfilled_TradeSuccessfullyCreated() {
        Order buyOrder = new Order();
        buyOrder.setId(3L);
        buyOrder.setUserId(4L);
        buyOrder.setSecurityId(1L);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setQuantity(30);
        buyOrder.setPrice(60);
        orderRepository.addOrder(buyOrder);

        tradeService.trade(buyOrder);
        assertEquals(1, tradeRepository.getAllTrades().size());
        assertEquals(Fulfilled.YES, buyOrder.getFulfilled());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenSellOrder_MatchingBuyOrderFound_SellOrderPartiallyFulfilled_TradeSuccessfullyCreated() {
        Order sellOrder = new Order();
        sellOrder.setId(2L);
        sellOrder.setUserId(5L);
        sellOrder.setSecurityId(1L);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setQuantity(100);
        sellOrder.setPrice(40);
        orderRepository.addOrder(sellOrder);

        tradeService.trade(sellOrder);
        assertEquals(1, tradeRepository.getAllTrades().size());
        assertEquals(Fulfilled.PARTIALLY, sellOrder.getFulfilled());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenSellOrder_MatchingBuyOrderFound_SellOrderFulfilled_TradeSuccessfullyCreated() {
        Order sellOrder = new Order();
        sellOrder.setId(3L);
        sellOrder.setUserId(6L);
        sellOrder.setSecurityId(1L);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setQuantity(50);
        sellOrder.setPrice(40);
        orderRepository.addOrder(sellOrder);

        tradeService.trade(sellOrder);
        assertEquals(1, tradeRepository.getAllTrades().size());
        assertEquals(Fulfilled.YES, sellOrder.getFulfilled());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenSellOrder_OrderWithMatchingPriceNotFound_TradeNotCreated() {
        Order sellOrder = new Order();
        sellOrder.setId(3L);
        sellOrder.setUserId(7L);
        sellOrder.setSecurityId(1L);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setQuantity(50);
        sellOrder.setPrice(1000);
        orderRepository.addOrder(sellOrder);

        tradeService.trade(sellOrder);
        assertEquals(0, tradeRepository.getAllTrades().size());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenBuyOrder_OrderWithMatchingSecurityIdNotFound_TradeNotCreated() {
        Order buyOrder = new Order();
        buyOrder.setId(3L);
        buyOrder.setUserId(8L);
        buyOrder.setSecurityId(2L);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setQuantity(50);
        buyOrder.setPrice(60);
        orderRepository.addOrder(buyOrder);

        tradeService.trade(buyOrder);
        assertEquals(0, tradeRepository.getAllTrades().size());
    }

    @SneakyThrows
    @Test
    void test_trade_GivenBuyOrder_OrderWithFulfilledNoOrPartiallyNotFound_TradeNotCreated() {
        Order buyOrder = new Order();
        buyOrder.setId(3L);
        buyOrder.setUserId(9L);
        buyOrder.setSecurityId(2L);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setQuantity(50);
        buyOrder.setPrice(60);
        orderRepository.addOrder(buyOrder);

        Order sellOrder = new Order();
        sellOrder.setId(3L);
        sellOrder.setFulfilled(Fulfilled.YES);
        sellOrder.setUserId(10L);
        sellOrder.setSecurityId(2L);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setQuantity(50);
        sellOrder.setPrice(50);
        orderRepository.addOrder(sellOrder);

        tradeService.trade(buyOrder);
        assertEquals(0, tradeRepository.getAllTrades().size());
    }
}

