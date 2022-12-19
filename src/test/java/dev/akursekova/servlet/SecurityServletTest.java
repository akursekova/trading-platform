package dev.akursekova.servlet;

import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.repository.SecurityRepository;
import dev.akursekova.service.TradeService;
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
class SecurityServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletContext servletContext;
    @Mock
    ServletConfig servletConfig;
    @Mock
    SecurityRepository securityRepository;
    @Mock
    SecurityServlet securityServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("securityRepository")).thenReturn(securityRepository);

        securityServlet = new SecurityServlet();
        securityServlet.init(servletConfig);
    }

    @Test
    void test_doGet_WhenSecurityWithGivenIdExists() throws ServletException, IOException, SecurityNotExistException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long securityId = 1L;

        Security security = new Security();
        security.setId(securityId);
        security.setName("WSB");

        Mockito.when(securityRepository.getSecurity(securityId)).thenReturn(security);

        securityServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
    }

    @Test
    void test_doGet_WhenSecurityWithGivenIdDoesNotExist() throws SecurityNotExistException, IOException, ServletException {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long securityId = 1L;
        SecurityNotExistException ex = new SecurityNotExistException("Security with given id = 1 doesn't exist");

        Mockito.when(securityRepository.getSecurity(securityId)).thenThrow(ex);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        String json = "{\n";
        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
        json += "}";

        securityServlet.doGet(request, response);

        assertThrows(SecurityNotExistException.class, () -> securityRepository.getSecurity(securityId));
        verify(printWriter, times(1)).println(json);
    }
}