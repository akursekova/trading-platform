package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.dto.CreatedUserDto;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import dev.akursekova.repository.UserRepositoryInterface;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

import org.json.JSONObject;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    private UserRepositoryInterface userRepository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        userRepository = (UserRepositoryInterface) context.getAttribute("userRepository");
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        String orderStr = request.getReader().lines().collect(Collectors.joining());
        User user = objectMapper.readValue(orderStr, User.class);

        userRepository.addUser(user);
        response.setStatus(202);

        CreatedUserDto createdUserDto = CreatedUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

        String userAsJson = objectMapper.writeValueAsString(createdUserDto);
        response.getWriter().println(userAsJson);

        LOG.debug("User with id = " + user.getId() + " successfully created: " + "\n" + userAsJson);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        Long userId = Long.parseLong(request.getPathInfo().substring(1));

        User user = userRepository.getUser(userId);
        response.setStatus(202);
        CreatedUserDto createdUserDto = CreatedUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
        String userAsJson = objectMapper.writeValueAsString(createdUserDto);
        response.getWriter().println(userAsJson);

        LOG.debug("Requested User with id = " + userId + ": " + "\n" + userAsJson);
    }
}
