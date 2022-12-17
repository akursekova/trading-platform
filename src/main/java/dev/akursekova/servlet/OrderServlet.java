package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.repository.OrderRepositoryInterface;
import dev.akursekova.repository.UserRepositoryInterface;
import dev.akursekova.service.TradingService;
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

    private OrderRepositoryInterface ordersRepository = null;
    private UserRepositoryInterface userRepository = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
//        buyOrdersRepository = (OrderRepositoryInterface) context.getAttribute("buyOrdersRepository");
//        sellOrdersRepository = (OrderRepositoryInterface) context.getAttribute("sellOrdersRepository");
        ordersRepository = (OrderRepositoryInterface) context.getAttribute("ordersRepository");
        userRepository = (UserRepositoryInterface) context.getAttribute("userRepository");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        int readChars;
        try(Reader reader = request.getReader()){
            while ((readChars=reader.read(buffer))!=-1){
                body.append(buffer,0, readChars);
            }
        }


        String orderStr = body.toString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Order order = mapper.readValue(orderStr, Order.class);

//TradingSersice.trade();




        try {
            //trandingService.trade(order);
            ordersRepository.addOrder(order);
            TradingService.trade(order, (OrderRepository) ordersRepository);
            response.setStatus(202);
        } catch(OrderCreationException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json); // TODO testing purpose, remove it later
        }

    }
}

// todo doGet TBD


/*
* new order: {domain}/orders
* new user: {domain}/users
* new security: {domain}/securities
* trade: POST?
* */
