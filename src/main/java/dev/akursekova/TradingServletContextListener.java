package dev.akursekova;

import dev.akursekova.repository.*;
import dev.akursekova.service.TradeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

@WebListener
public class TradingServletContextListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(TradingServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("TradingServletContextListener started");
        ServletContext context = event.getServletContext();

        UserRepositoryInterface userRepository = new UserRepository(new HashMap<>());
        context.setAttribute("userRepository", userRepository);
        LOG.info("userRepository has been created: " + userRepository);

        SecurityRepositoryInterface securityRepository = new SecurityRepository(new HashMap<>());
        context.setAttribute("securityRepository", securityRepository);
        LOG.info("securityRepository has been created: " + securityRepository);

        OrderRepositoryInterface orderRepository = new OrderRepository(new HashMap<>());
        context.setAttribute("orderRepository", orderRepository);
        LOG.info("orderRepository has been created: " + orderRepository);


        TradeRepositoryInterface tradeRepository = new TradeRepository(new HashMap<>());
        context.setAttribute("tradeRepository", tradeRepository);
        LOG.info("tradeRepository has been created: " + tradeRepository);

        TradeService tradeService = new TradeService(tradeRepository, orderRepository);
        context.setAttribute("tradeService", tradeService);

    }
}
