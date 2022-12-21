package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class OrderService implements OrderServiceInterface {
    private static final Logger LOG = LogManager.getLogger(OrderService.class);

    private final OrderRepositoryInterface orderRepository;
    private final UserRepositoryInterface userRepository;
    private final SecurityRepositoryInterface securityRepository;

    public OrderService(OrderRepositoryInterface orderRepository,
                        UserRepositoryInterface userRepository,
                        SecurityRepositoryInterface securityRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    @Override
    public void validateAndCreateOrder(Order order) throws OrderCreationException {
        if (!userWithProvidedIdExist(order.getUserId())) {
            LOG.error("Order cannot be created: user with provided userId = '"
                    + order.getUserId() + "' doesn't exist");
            throw new OrderCreationException("Order cannot be created: user with provided userId = '"
                    + order.getUserId() + "' doesn't exist");
        }
        if (!securityWithProvidedIdExist(order.getSecurityId())) {
            LOG.error("Order cannot be created: security with provided securityId = '"
                    + order.getSecurityId() + "' doesn't exist");
            throw new OrderCreationException("Order cannot be created: security with provided securityId = '"
                    + order.getSecurityId() + "' doesn't exist");
        }
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
        orderRepository.addOrder(order);
        LOG.debug("New Order created: " + order.toString());
    }


    private boolean userWithProvidedIdExist(Long userId) {
        return !userRepository.getAllUsers()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .equals(Optional.empty());
    }

    private boolean securityWithProvidedIdExist(Long securityId) {
        return !securityRepository.getAllSecurities()
                .stream()
                .filter(s -> s.getId() == securityId)
                .findFirst()
                .equals(Optional.empty());
    }
}
