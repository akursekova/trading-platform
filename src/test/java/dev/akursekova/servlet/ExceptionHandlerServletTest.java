package dev.akursekova.servlet;

import dev.akursekova.repository.SecurityRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletConfig servletConfig;
    @Mock
    SecurityRepository securityRepository;
    @Mock
    PrintWriter printWriter;
    @Mock
    Throwable throwable;

    ExceptionHandlerServlet exceptionHandlerServlet;

    @BeforeEach
    void setup() throws ServletException {
        exceptionHandlerServlet = new ExceptionHandlerServlet();
        exceptionHandlerServlet.init(servletConfig);
    }

    @SneakyThrows
    @Test
    void test_doGet_WhenSecurityWithGivenIdDoesNotExist_ShouldThrowException() {
        String errorMessage = "security with id = 1 doesn't exist";
        String errorAsJson = "{\"errorMessage\":\"security with id = 1 doesn't exist\"}";


        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(throwable.getMessage()).thenReturn(errorMessage);
        Mockito.when(request.getAttribute("javax.servlet.error.exception")).thenReturn(throwable);

        exceptionHandlerServlet.doGet(request, response);

        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).println(errorAsJson);
    }

    @SneakyThrows
    @Test
    void test_doPost_EmptySecurityNameInRequest_ShouldThrowException() {
        String errorMessage = "empty security name";
        String errorAsJson = "{\"errorMessage\":\"empty security name\"}";


        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(throwable.getMessage()).thenReturn(errorMessage);
        Mockito.when(request.getAttribute("javax.servlet.error.exception")).thenReturn(throwable);

        exceptionHandlerServlet.doPost(request, response);

        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).println(errorAsJson);
    }

}