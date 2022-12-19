package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.service.TradeService;
import lombok.SneakyThrows;
import org.json.JSONObject;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

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
    TradeService tradeService;
    OrderServlet orderServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("orderRepository")).thenReturn(orderRepository);
        Mockito.when(servletContext.getAttribute("tradeService")).thenReturn(tradeService);


        orderServlet = new OrderServlet();
        orderServlet.init(servletConfig);
    }

//    @Test
//    void test_doPost_SuccessfulOrderCreation() throws ServletException, IOException, OrderCreationException {
//
////        StringBuilder body = new StringBuilder();
////        body.append("{\"userId\":1,\"securityId\":1,\"type\":\"sell\",\"price\":\"50\",\"quantity\":\"10\"}");
////        String orderStr = body.toString();
////        Mockito.when(body.toString()).thenReturn(orderStr);
////Mockito.when(body.toString()).thenReturn(orderStr);
//
//
////        char[] buffer = orderStr.toCharArray();
////        Reader reader = Mockito.mock(Reader.class);
////        int readChars = -1;
////        Mockito.when(reader.read(buffer)).thenReturn(readChars);
//
//        Order order = new Order();
//        order.setId(1);
//        order.setSecurityId(1);
//        order.setType(OrderType.SELL);
//        order.setPrice(50);
//        order.setQuantity(10);
//
//        orderServlet.doPost(request, response);
//
//        verify(orderRepository, times(1)).addOrder(order);
//        verify(tradeService, times(1)).trade(order);
//        verify(response, times(1)).setStatus(202);
//    }

    @Test
    void test_doGet_WhenOrderWithGivenIdExists() throws OrderNotExistException, ServletException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");

        Long orderId = 1L;

        Order order = new Order();
        order.setId(1);
        order.setSecurityId(1);
        order.setType(OrderType.SELL);
        order.setPrice(50);
        order.setQuantity(10);

        Mockito.when(orderRepository.getOrder(orderId)).thenReturn(order);

        orderServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
    }

    @Test
    void test_doGet_WhenOrderWithGivenIdDoesNotExist() throws ServletException, IOException, OrderNotExistException {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long orderId = 1L;
        OrderNotExistException ex = new OrderNotExistException("order with id = 1 doesn't exist");

        Mockito.when(orderRepository.getOrder(orderId)).thenThrow(ex);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        String json = "{\n";
        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
        json += "}";

        orderServlet.doGet(request, response);

        assertThrows(OrderNotExistException.class, () -> orderRepository.getOrder(orderId));
        verify(printWriter, times(1)).println(json);
    }

}