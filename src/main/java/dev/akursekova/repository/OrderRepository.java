package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository implements OrderRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(OrderRepository.class);
    private static AtomicLong orderId = new AtomicLong(0L);

    public Map<Long, Order> orders = new HashMap<>(); // todo not sure if public is correct

    @Override
    public void addOrder(Order order) throws OrderCreationException {
        if (userWithProvidedIdDoesNotExist(order)){
            LOG.error("Order cannot be created: user with provided userId = '"
                    + order.getUserId() + "' doesn't exist");
            throw new OrderCreationException("Order cannot be created: user with provided userId = '"
                    + order.getUserId()
                    + "' doesn't exist");
        }
        if (securityWithProvidedIdDoesNotExist(order)){
            LOG.error("Order cannot be created: security with provided securityId = '"
                    + order.getUserId() + "' doesn't exist");
            throw new OrderCreationException("Order cannot be created: security with provided securityId = '"
                    + order.getUserId()
                    + "' doesn't exist");
        }
        if (order.getQuantity() <= 0){
            LOG.error("Order cannot be created: incorrect quantity specified. quantity = " + order.getQuantity());
            throw new OrderCreationException("Order cannot be created: incorrect quantity specified. quantity = "
                    + order.getQuantity());
        }
        if (order.getPrice() <= 0){
            LOG.error("Order cannot be created: incorrect price specified. price = " + order.getPrice());
            throw new OrderCreationException("Order cannot be created: incorrect price specified. price = "
                    + order.getPrice());
        }
        order.setId(orderId.incrementAndGet());
        orders.put(order.getId(), order);
        LOG.debug("New Order created: " + order.toString());

    }

    private boolean userWithProvidedIdDoesNotExist(Order order) {
        // todo to handle situation when POST request has userID which doesn't exist in the system
        return  false;
    }

    private boolean securityWithProvidedIdDoesNotExist(Order order) {
        // todo to handle situation when POST request has securityId which doesn't exist in the system
        return false;
    }

//    @Override
//    public void showOrders(){
//        for (Map.Entry<Long, Order> entry : orders.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
//    }
}
