package dev.akursekova.servlet;

import dev.akursekova.entities.Security;
import dev.akursekova.entities.User;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.exception.UserNotExistException;
import dev.akursekova.repository.OrderRepository;
import dev.akursekova.repository.UserRepository;
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
class UserServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletContext servletContext;
    @Mock
    ServletConfig servletConfig;
    @Mock
    UserRepository userRepository;

    UserServlet userServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);


        userServlet = new UserServlet();
        userServlet.init(servletConfig);
    }

    @Test
    void test_doGet_WhenUserWithGivenIdExists() throws UserNotExistException, ServletException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("name1");
        user.setPassword("pswd1");

        Mockito.when(userRepository.getUser(userId)).thenReturn(user);

        userServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
    }

    @Test
    void test_doGet_WhenUserWithGivenIdDoesNotExist() throws UserNotExistException, IOException, ServletException {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long userId = 1L;
        UserNotExistException ex = new UserNotExistException("User with given id = 1 doesn't exist");

        Mockito.when(userRepository.getUser(userId)).thenThrow(ex);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        String json = "{\n";
        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
        json += "}";

        userServlet.doGet(request, response);

        assertThrows(UserNotExistException.class, () -> userRepository.getUser(userId));
        verify(printWriter, times(1)).println(json);
    }

}