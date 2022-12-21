package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Trade;
import dev.akursekova.exception.TradeNotExistException;
import dev.akursekova.repository.TradeRepositoryInterface;
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

@WebServlet(name = "TradeServlet", value = "/trades/*")
public class TradeServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(TradeServlet.class);

    private TradeRepositoryInterface tradeRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        tradeRepository = (TradeRepositoryInterface) context.getAttribute("tradeRepository");
    }


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Long tradeId = Long.parseLong(request.getPathInfo().substring(1));

        Trade trade = tradeRepository.getTrade(tradeId);
        response.setStatus(202);
        String tradeAsJson = objectMapper.writeValueAsString(trade);
        response.getWriter().println(tradeAsJson);

        LOG.debug("Requested Trade with id = " + tradeId + ": " + "\n" + tradeAsJson);


    }
}
