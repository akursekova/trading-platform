package dev.akursekova.servlet;

import dev.akursekova.entities.Trade;
import dev.akursekova.exception.TradeNotExistException;
import dev.akursekova.repository.TradeRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletContext servletContext;
    @Mock
    ServletConfig servletConfig;
    @Mock
    TradeRepository tradeRepository;

    TradeServlet tradeServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("tradeRepository")).thenReturn(tradeRepository);


        tradeServlet = new TradeServlet();
        tradeServlet.init(servletConfig);
    }

    @Test
    void test_doGet_WhenUserWithGivenIdExists() throws ServletException, IOException, TradeNotExistException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long tradeId = 1L;

        Trade trade = new Trade(1L, 2L, 50, 100);
        trade.setId(tradeId);


        Mockito.when(tradeRepository.getTrade(tradeId)).thenReturn(trade);

        tradeServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
    }

    @Test
    void test_doGet_WhenTradeWithGivenIdDoesNotExist() throws TradeNotExistException, IOException, ServletException {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long tradeId = 1L;
        TradeNotExistException ex = new TradeNotExistException("Trade with given id = 1 doesn't exist");

        Mockito.when(tradeRepository.getTrade(tradeId)).thenThrow(ex);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        String json = "{\n";
        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
        json += "}";

        tradeServlet.doGet(request, response);

        assertThrows(TradeNotExistException.class, () -> tradeRepository.getTrade(tradeId));
        verify(printWriter, times(1)).println(json);
    }

}