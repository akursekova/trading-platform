package dev.akursekova.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.dto.CreatedOrderDto;
import dev.akursekova.entities.Order;
import dev.akursekova.entities.Trade;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import dev.akursekova.repository.OrderRepositoryInterface;
import dev.akursekova.service.OrderService;
import dev.akursekova.service.OrderServiceInterface;
import dev.akursekova.service.TradeService;
import dev.akursekova.service.TradeServiceInterface;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "OrderServlet", value = "/orders/*")
public class OrderServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(OrderServlet.class);

    private OrderRepositoryInterface orderRepository;
    private TradeServiceInterface tradeService;
    private OrderServiceInterface orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();

        orderRepository = (OrderRepositoryInterface) context.getAttribute("orderRepository");
        tradeService = (TradeServiceInterface) context.getAttribute("tradeService");
        orderService = (OrderServiceInterface) context.getAttribute("orderService");
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        String orderStr = request.getReader().lines().collect(Collectors.joining());
        Order order = null;

        try {
            order = objectMapper.readValue(orderStr, Order.class);
        } catch (JsonProcessingException e) {
            LOG.error("Order cannot be created: type is empty");
            throw new OrderCreationException("Order cannot be created: type is empty");
        }

        orderService.validateOrder(order);
        Trade trade = tradeService.trade(order);
        response.setStatus(202);

        CreatedOrderDto createdOrderDto = CreatedOrderDto.builder()
                .order(order)
                .trade(trade)
                .build();
        String orderAsJson = objectMapper.writeValueAsString(createdOrderDto);
        response.getWriter().println(orderAsJson);

        LOG.debug("Order with id = " + order.getId() + " successfully created: " + "\n" + orderAsJson);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Long orderId = Long.parseLong(request.getPathInfo().substring(1));

        Order order = orderRepository.getOrder(orderId);
        response.setStatus(202);

        String orderAsJson = objectMapper.writeValueAsString(order);
        response.getWriter().println(orderAsJson);
        LOG.debug("Requested Order with id = " + orderId + ":" + "\n" + orderAsJson);
    }
}


