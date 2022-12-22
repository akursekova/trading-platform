package dev.akursekova.service;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.entities.Security;
import dev.akursekova.entities.User;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.repository.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderServiceInterface orderService;
    private OrderRepositoryInterface orderRepository;
    private Map<Long, Order> orders;
    private UserRepositoryInterface userRepository;
    private Map<Long, User> users;
    private SecurityRepositoryInterface securityRepository;
    private Map<Long, Security> securities;


    @SneakyThrows
    @BeforeEach
    void setup() {
        users = new HashMap<>();
        userRepository = new UserRepository(users);

        securities = new HashMap<>();
        securityRepository = new SecurityRepository(securities);

        orders = new HashMap<>();
        orderRepository = new OrderRepository(orders);

        this.orderService = new OrderService(orderRepository, userRepository, securityRepository);

        User user = new User(1L, "testname", "testpswd");
        userRepository.addUser(user);


        Security security = new Security();
        security.setId(1L);
        security.setName("test");
        securityRepository.addSecurity(security);

    }

    @Test
    void test_validateOrder_UserWithProvidedIdDoesnNotExist_ShouldThrowOrderCreationException() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(3L);
        order.setSecurityId(1L);
        order.setType(OrderType.SELL);
        order.setQuantity(50);
        order.setPrice(100);

        assertThrows(OrderCreationException.class,
                () -> orderService.validateAndCreateOrder(order));
    }

    @Test
    void test_validateOrder_SecurityWithProvidedIdDoesnNotExist_ShouldThrowOrderCreationException() {
        User user = userRepository.getAllUsers().stream().findFirst().get();

        Order order = new Order();
        order.setId(1L);
        order.setUserId(user.getId());
        order.setSecurityId(Long.MAX_VALUE);
        order.setType(OrderType.SELL);
        order.setQuantity(50);
        order.setPrice(100);

        assertThrows(OrderCreationException.class,
                () -> orderService.validateAndCreateOrder(order));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -100})
    void test_validateOrder_QuantityLessThanOrEqualToZero_ShouldThrowOrderCreationException(int argument) {
        User user = userRepository.getAllUsers().stream().findFirst().get();
        Security security = securityRepository.getAllSecurities().stream().findFirst().get();

        Order order = new Order();
        order.setId(1L);
        order.setUserId(user.getId());
        order.setSecurityId(security.getId());
        order.setType(OrderType.SELL);
        order.setQuantity(argument);
        order.setPrice(100);

        assertThrows(OrderCreationException.class, () -> orderService.validateAndCreateOrder(order));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, -100})
    void test_validateOrder_PriceLessThanOrEqualToZero_ShouldThrowOrderCreationException(int argument) {
        User user = userRepository.getAllUsers().stream().findFirst().get();
        Security security = securityRepository.getAllSecurities().stream().findFirst().get();

        Order order = new Order();
        order.setId(1L);
        order.setUserId(user.getId());
        order.setSecurityId(security.getId());
        order.setType(OrderType.SELL);
        order.setQuantity(50);
        order.setPrice(argument);

        assertThrows(OrderCreationException.class, () -> orderService.validateAndCreateOrder(order));
    }

    @SneakyThrows
    @Test
    void test_validateAndCreateOrder_AllValidationsPass() {
        User user = userRepository.getAllUsers().stream().findFirst().get();
        Security security = securityRepository.getAllSecurities().stream().findFirst().get();

        Order order = new Order();
        order.setId(1L);
        order.setUserId(user.getId());
        order.setSecurityId(security.getId());
        order.setType(OrderType.SELL);
        order.setQuantity(50);
        order.setPrice(100);

        orderService.validateAndCreateOrder(order);

        assertEquals(1, orderRepository.getAllOrders().size());
    }

}
