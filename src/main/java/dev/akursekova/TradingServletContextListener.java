package dev.akursekova;

import dev.akursekova.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TradingServletContextListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(TradingServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("TradingServletContextListener started");
        ServletContext context = event.getServletContext();

        UserRepositoryInterface userRepository = new UserRepository();
        context.setAttribute("userRepository", userRepository);
        LOG.info("userRepository has been created: " + userRepository);

        SecurityRepositoryInterface securityRepository = new SecurityRepository();
        context.setAttribute("securityRepository", securityRepository);
        LOG.info("securityRepository has been created: " + securityRepository);


        /*OrderRepositoryInterface buyOrdersRepository = new OrderRepository();
        context.setAttribute("buyOrdersRepository", buyOrdersRepository);
        LOG.info("buyOrdersRepository has been created: " + buyOrdersRepository);

        OrderRepositoryInterface sellOrdersRepository = new OrderRepository();
        context.setAttribute("sellOrdersRepository", sellOrdersRepository);
        LOG.info("sellOrdersRepository has been created: " + sellOrdersRepository);*/

        OrderRepositoryInterface ordersRepository = new OrderRepository();
        context.setAttribute("ordersRepository", ordersRepository);
        LOG.info("ordersRepository has been created: " + ordersRepository);
    }
}
