package dev.akursekova.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.service.OrderService;
import dev.akursekova.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletContext servletContext;
    @Mock
    ServletConfig servletConfig;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderService orderService;
    @Mock
    TradeService tradeService;
    @Mock
    PrintWriter printWriter;
    OrderServlet orderServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("orderRepository")).thenReturn(orderRepository);
        Mockito.when(servletContext.getAttribute("tradeService")).thenReturn(tradeService);
        Mockito.when(servletContext.getAttribute("orderService")).thenReturn(orderService);


        orderServlet = new OrderServlet();

        orderServlet.init(servletConfig);
    }

    @Test
    void test_doPost_SuccessfulOrderCreation() throws IOException, OrderCreationException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);

        Stream<String> str = "{\"userId\":1,\"securityId\":1,\"type\":\"sell\",\"price\":\"50\",\"quantity\":\"10\"}".lines();

        Order order = new Order();
        order.setUserId(1);
        order.setSecurityId(1);
        order.setType(OrderType.SELL);
        order.setPrice(50);
        order.setQuantity(10);

        String createdOrderDto = "{\"order\":{\"id\":0,\"userId\":1,\"securityId\":1,\"type\":\"SELL\",\"price\":50," +
                "\"quantity\":10,\"fulfilled\":\"NO\"},\"trade\":null}";

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(bufferedReader.lines()).thenReturn(str);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(tradeService.trade(order)).thenReturn(null);


        orderServlet.doPost(request, response);

        verify(orderService, times(1)).validateAndCreateOrder(order);
        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(createdOrderDto);
    }

    @Test
    void test_doPost_EmptyOrderTypeInRequest_ShouldThrowOrderCreationException() throws IOException, OrderCreationException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

        String message = "Order cannot be created: type is empty";

        InvalidFormatException ex = Mockito.mock(InvalidFormatException.class);

        String orderStr = "{\"userId\":1,\"securityId\":1,\"type\":\"\",\"price\":\"50\",\"quantity\":\"100\"}";

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        doThrow(ex).when(objectMapper).readValue(orderStr, Order.class);

        orderServlet.doPost(request, response);

        assertThrows(JsonProcessingException.class, () -> objectMapper.readValue(orderStr, Order.class));
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).println(message);
    }

    @Test
    void test_doGet_WhenOrderWithGivenIdExists() throws OrderNotExistException, ServletException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");

        Long orderId = 1L;

        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setSecurityId(1);
        order.setType(OrderType.SELL);
        order.setPrice(50);
        order.setQuantity(10);

        String orderAsJson = "{\"id\":1,\"userId\":1,\"securityId\":1,\"type\":\"SELL\"," +
                "\"price\":50,\"quantity\":10,\"fulfilled\":\"NO\"}";

        Mockito.when(orderRepository.getOrder(orderId)).thenReturn(order);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        orderServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(orderAsJson);
    }
}