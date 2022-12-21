package dev.akursekova.servlet;

import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import dev.akursekova.repository.UserRepository;
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

    @Mock
    PrintWriter printWriter;

    UserServlet userServlet;

    @BeforeEach
    void setup() throws ServletException {
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);

        userServlet = new UserServlet();
        userServlet.init(servletConfig);
    }

    @Test
    void test_doPost_SuccessfulUserCreation() throws IOException, UserCreationException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);

        Stream<String> str = "{\"username\":\"testuser1\",\"password\":\"password\"}".lines();


        User user = new User();
        user.setUsername("testuser1");
        user.setPassword("password");

        String userAsJson = "{\"id\":0,\"username\":\"testuser1\"}";

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(bufferedReader.lines()).thenReturn(str);

        userServlet.doPost(request, response);

        verify(userRepository, times(1)).addUser(user);
        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(userAsJson);
    }

    @Test
    void test_doGet_WhenUserWithGivenIdExists() throws UserNotExistException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("name1");
        user.setPassword("pswd1");

        String userAsJson = "{\"id\":1,\"username\":\"name1\"}";

        Mockito.when(userRepository.getUser(userId)).thenReturn(user);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        userServlet.doGet(request, response);

        verify(response, times(1)).setStatus(202);
        verify(printWriter, times(1)).println(userAsJson);
    }

}
