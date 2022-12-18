package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository implements OrderRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(OrderRepository.class);

    protected Map<Long, Order> orders;
    private static AtomicLong orderId = new AtomicLong(0L);

    public OrderRepository(Map<Long, Order> orders){
        this.orders = orders;
    }

    @Override
    public void addOrder(Order order) throws OrderCreationException {
//        if (userWithProvidedIdDoesNotExist(order)){
//            LOG.error("Order cannot be created: user with provided userId = '"
//                    + order.getUserId() + "' doesn't exist");
//            throw new OrderCreationException("Order cannot be created: user with provided userId = '"
//                    + order.getUserId()
//                    + "' doesn't exist");
//        }
//        if (securityWithProvidedIdDoesNotExist(order)){
//            LOG.error("Order cannot be created: security with provided securityId = '"
//                    + order.getUserId() + "' doesn't exist");
//            throw new OrderCreationException("Order cannot be created: security with provided securityId = '"
//                    + order.getUserId()
//                    + "' doesn't exist");
//        }
        if (order.getQuantity() <= 0) {
            LOG.error("Order cannot be created: incorrect quantity specified. quantity = " + order.getQuantity());
            throw new OrderCreationException("Order cannot be created: incorrect quantity specified. quantity = "
                    + order.getQuantity());
        }
        if (order.getPrice() <= 0) {
            LOG.error("Order cannot be created: incorrect price specified. price = " + order.getPrice());
            throw new OrderCreationException("Order cannot be created: incorrect price specified. price = "
                    + order.getPrice());
        }
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

//    private boolean userWithProvidedIdDoesNotExist(Order order) {
//        // todo to handle situation when POST request has userID which doesn't exist in the system
//        return  false;
//    }
//
//    private boolean securityWithProvidedIdDoesNotExist(Order order) {
//        // todo to handle situation when POST request has securityId which doesn't exist in the system
//        return false;
//    }

//    @Override
//    public void showOrders(){
//        for (Map.Entry<Long, Order> entry : orders.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
//    }

    public Collection<Order> getAllOrders(){
        return orders.values();
    }
}
