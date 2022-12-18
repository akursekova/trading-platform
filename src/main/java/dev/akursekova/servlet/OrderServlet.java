package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Order;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.OrderNotExistException;
import dev.akursekova.repository.OrderRepositoryInterface;
import dev.akursekova.repository.UserRepositoryInterface;
import dev.akursekova.service.TradeService;
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
import java.io.Reader;

@WebServlet(name = "OrderServlet", value = "/orders/*")
public class OrderServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(OrderServlet.class);

//    private OrderRepositoryInterface buyOrdersRepository = null;
//    private OrderRepositoryInterface sellOrdersRepository = null;

    private OrderRepositoryInterface ordersRepository;
    private UserRepositoryInterface userRepository;
    private TradeService tradeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();

        ordersRepository = (OrderRepositoryInterface) context.getAttribute("ordersRepository");
        userRepository = (UserRepositoryInterface) context.getAttribute("userRepository");
        tradeService = (TradeService) context.getAttribute("tradeService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        int readChars;
        try (Reader reader = request.getReader()) {
            while ((readChars = reader.read(buffer)) != -1) {
                body.append(buffer, 0, readChars);
            }
        }


        String orderStr = body.toString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Order order = mapper.readValue(orderStr, Order.class);

        try {
            ordersRepository.addOrder(order);
            tradeService.trade(order);
            response.setStatus(202);
        } catch (OrderCreationException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json); // TODO testing purpose, remove it later
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);

        Long orderId = Long.parseLong(request.getPathInfo().substring(1));

        try {
            Order order = ordersRepository.getOrder(orderId);
            response.setStatus(202);
            String json = "{\n";
            json += "\"id\": " + JSONObject.quote(String.valueOf(order.getId())) + ",\n";
            json += "\"userId\": " + JSONObject.quote(String.valueOf(order.getUserId())) + ",\n";
            json += "\"securityId\": " + JSONObject.quote(String.valueOf(order.getSecurityId())) + ",\n";
            json += "\"type\": " + JSONObject.quote(String.valueOf(order.getType())) + ",\n";
            json += "\"price\": " + JSONObject.quote(String.valueOf(order.getPrice())) + ",\n";
            json += "\"quantity\": " + JSONObject.quote(String.valueOf(order.getQuantity())) + ",\n";
            json += "\"fulfilled\": " + JSONObject.quote(String.valueOf(order.getFulfilled())) + "\n";
            json += "}";
            System.out.println(json); // todo testing purposes
        } catch (OrderNotExistException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json);
        }
    }
}




/*
 * new order: {domain}/orders
 * new user: {domain}/users
 * new security: {domain}/securities
 * trade: POST?
 * */
