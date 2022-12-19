package dev.akursekova.servlet;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.OrderType;
import dev.akursekova.entities.Security;
import dev.akursekova.exception.OrderCreationException;
import dev.akursekova.exception.SecurityCreationException;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void test_doPost_SuccessfulSecurityCreation() throws SecurityCreationException, ServletException, IOException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);

        Stream<String> str = "{\"name\":\"WSB\"}".lines();

        Security security = new Security();
        security.setName("WSB");

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(bufferedReader.lines()).thenReturn(str);

        securityServlet.doPost(request, response);

        verify(securityRepository, times(1)).addSecurity(security);
        verify(response, times(1)).setStatus(202);
    }

    @Test
    void test_doPost_EmptyNameInRequest_ShouldThrowSecurityCreationException() throws ServletException, IOException, SecurityCreationException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

        SecurityCreationException ex = new SecurityCreationException("empty security name");

        Stream<String> str = "{\"name\":\"\"}".lines();

        String json = "{\n";
        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
        json += "}";

        Security security = new Security();
        security.setName("");

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(bufferedReader.lines()).thenReturn(str);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        doThrow(ex).when(securityRepository).addSecurity(security);

        securityServlet.doPost(request, response);

        verify(securityRepository, times(1)).addSecurity(security);
        assertThrows(SecurityCreationException.class, () -> securityRepository.addSecurity(security));
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).println(json);
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