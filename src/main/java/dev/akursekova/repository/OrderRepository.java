package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository implements OrderRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(OrderRepository.class);

    protected Map<Long, Order> orders;
    private static AtomicLong orderId = new AtomicLong(0L);

    public OrderRepository(Map<Long, Order> orders) {
        this.orders = orders;
    }

    @Override
    public void addOrder(Order order) {
        order.setId(orderId.incrementAndGet());
        orders.put(order.getId(), order);

        LOG.debug("New Order created: " + order.toString());
    }

    @Override
    public Order getOrder(Long id) throws OrderNotExistException {
        if (!orders.containsKey(id)) {
            LOG.error("Order with given id = " + id + " doesn't exist");
            throw new OrderNotExistException("order with id = " + id + " doesn't exist");
        }
        return orders.get(id);
    }

    @Override
    public Collection<Order> getAllOrders() {
        return orders.values();
    }
}
