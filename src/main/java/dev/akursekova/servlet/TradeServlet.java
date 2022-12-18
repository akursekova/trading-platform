package dev.akursekova.servlet;

import dev.akursekova.entities.Trade;
import dev.akursekova.exception.TradeNotExistException;
import dev.akursekova.repository.TradeRepositoryInterface;
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

@WebServlet(name = "TradeServlet", value = "/trades/*")
public class TradeServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(TradeServlet.class);

    private TradeRepositoryInterface tradeRepository = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        tradeRepository = (TradeRepositoryInterface) context.getAttribute("tradeRepository");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);

        Long tradeId = Long.parseLong(request.getPathInfo().substring(1));

        try {
            Trade trade = tradeRepository.getTrade(tradeId);
            response.setStatus(202);
            String json = "{\n";
            json += "\"id\": " + JSONObject.quote(String.valueOf(trade.getId())) + ",\n";
            json += "\"sellOrderId\": " + JSONObject.quote(String.valueOf(trade.getSellOrderId())) + ",\n";
            json += "\"buyOrderId\": " + JSONObject.quote(String.valueOf(trade.getBuyOrderId())) + ",\n";
            json += "\"price\": " + JSONObject.quote(String.valueOf(trade.getPrice())) + ",\n";
            json += "\"quantity\": " + JSONObject.quote(String.valueOf(trade.getQuantity())) + "\n";
            json += "}";
            System.out.println(json); // TODO testing purpose, remove it later
        } catch (TradeNotExistException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json); // TODO testing purpose, remove it later
        }


    }
}
