package dev.akursekova.servlet;

import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.repository.SecurityRepository;
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
    PrintWriter printWriter;
    SecurityServlet securityServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("securityRepository")).thenReturn(securityRepository);

        securityServlet = new SecurityServlet();
        securityServlet.init(servletConfig);
    }

    @Test
    void test_doPost_SuccessfulSecurityCreation() throws SecurityCreationException, IOException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);

        Stream<String> str = "{\"name\":\"WSB\"}".lines();

        Security security = new Security();
        security.setName("WSB");

        String securityAsJson = "{\"id\":0,\"name\":\"WSB\"}";

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(bufferedReader.lines()).thenReturn(str);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        securityServlet.doPost(request, response);

        verify(securityRepository, times(1)).addSecurity(security);
        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(securityAsJson);
    }

    @Test
    void test_doGet_WhenSecurityWithGivenIdExists() throws ServletException, IOException, SecurityNotExistException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long securityId = 1L;

        Security security = new Security();
        security.setId(securityId);
        security.setName("WSB");

        String securityAsJson = "{\"id\":1,\"name\":\"WSB\"}";

        Mockito.when(securityRepository.getSecurity(securityId)).thenReturn(security);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        securityServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(securityAsJson);
    }
}
