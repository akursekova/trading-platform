package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {
    private Map<Long, Order> orders;
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orders = new HashMap<>();
        this.orderRepository = new OrderRepository(orders);

        Order order = new Order();
        order.setPrice(50);
        order.setQuantity(100);
        orders.put(2L, order);
    }

//    @ParameterizedTest
//    @ValueSource(ints = {0, -100})
//    void test_addOrder_QuantityLessThanOrEqualToZero_ShouldThrowOrderCreationException(int argument) {
//        Order order = new Order();
//        order.setQuantity(argument);
//        assertThrows(OrderCreationException.class, () -> orderRepository.addOrder(order));
//    }

//    @ParameterizedTest
//    @ValueSource(ints = {0, -100})
//    void test_addOrder_PriceLessThanOrEqualToZero_ShouldThrowOrderCreationException(int argument) {
//        Order order = new Order();
//        order.setQuantity(50);
//        order.setPrice(argument);
//        assertThrows(OrderCreationException.class, () -> orderRepository.addOrder(order));
//    }

    @SneakyThrows
    @Test
    void test_addOrder_NewOrder() {
        Order order = new Order();
        order.setQuantity(100);
        order.setPrice(50);
        orderRepository.addOrder(order);

        assertEquals(2, orderRepository.orders.size());
    }

    @SneakyThrows
    @Test
    void test_getOrder_WhenIdIsPresent() {
        Order expectedOrder = orders.get(2L);
        assertEquals(expectedOrder, orderRepository.getOrder(2L));
    }

    @Test
    void test_getOrder_WhenIdIsNotPresent_ShouldThrowOrderNotExistException() {
        assertThrows(OrderNotExistException.class,
                () -> orderRepository.getOrder(1L));
    }

    @Test
    void test_getAllOrders_ShouldReturnWholeCollection() {
        Collection<Order> expectedOrders = orderRepository.orders.values();
        assertEquals(expectedOrders, orderRepository.getAllOrders());
    }
}