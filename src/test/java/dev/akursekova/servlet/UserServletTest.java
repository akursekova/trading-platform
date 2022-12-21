package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.dto.CreatedUserDto;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import dev.akursekova.repository.UserRepository;
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
    void test_doPost_SuccessfulUserCreation() throws ServletException, IOException, UserCreationException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

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

//    @Test
//    void test_doPost_EmptyPasswordInRequest_ShouldThrowUserCreationException() throws IOException, UserCreationException, ServletException {
//        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
//        PrintWriter printWriter = Mockito.mock(PrintWriter.class);
//
//        UserCreationException ex = new UserCreationException("empty password");
//
//        Stream<String> str = "{\"username\":\"testuser1\",\"password\":\"\"}".lines();
//
//        String json = "{\n";
//        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
//        json += "}";
//
//        User user = new User();
//        user.setUsername("testuser1");
//        user.setPassword("");
//
//        Mockito.when(request.getReader()).thenReturn(bufferedReader);
//        Mockito.when(bufferedReader.lines()).thenReturn(str);
//        Mockito.when(response.getWriter()).thenReturn(printWriter);
//        doThrow(ex).when(userRepository).addUser(user);
//
//        userServlet.doPost(request, response);
//
//        verify(userRepository, times(1)).addUser(user);
//        assertThrows(UserCreationException.class, () -> userRepository.addUser(user));
//        verify(response, times(1)).setStatus(400);
//        verify(printWriter, times(1)).println(json);
//    }

    @Test
    void test_doGet_WhenUserWithGivenIdExists() throws UserNotExistException, ServletException, IOException {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);

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

//    @Test
//    void test_doGet_WhenUserWithGivenIdDoesNotExist() throws UserNotExistException, IOException, ServletException {
//        PrintWriter printWriter = Mockito.mock(PrintWriter.class);
//
//        Mockito.when(request.getPathInfo()).thenReturn("/1");
//        Long userId = 1L;
//        UserNotExistException ex = new UserNotExistException("User with given id = 1 doesn't exist");
//
//        Mockito.when(userRepository.getUser(userId)).thenThrow(ex);
//        Mockito.when(response.getWriter()).thenReturn(printWriter);
//
//        String json = "{\n";
//        json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
//        json += "}";
//
//        userServlet.doGet(request, response);
//
//        assertThrows(UserNotExistException.class, () -> userRepository.getUser(userId));
//        verify(printWriter, times(1)).println(json);
//    }

}