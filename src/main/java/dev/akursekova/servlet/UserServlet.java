package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import dev.akursekova.repository.UserRepositoryInterface;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderStr = request.getReader().lines().collect(Collectors.joining());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        User user = mapper.readValue(orderStr, User.class);

        try {
            userRepository.addUser(user);
            response.setStatus(202);
        } catch (UserCreationException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json); // TODO testing purpose, remove it later
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long userId = Long.parseLong(request.getPathInfo().substring(1));

        try {
            User user = userRepository.getUser(userId);
            response.setStatus(202);
            String json = "{\n";
            json += "\"id\": " + JSONObject.quote(String.valueOf(user.getId())) + ",\n";
            json += "\"username\": " + JSONObject.quote(user.getUsername()) + ",\n";
            json += "\"password\": " + JSONObject.quote(user.getPassword()) + "\n";
            json += "}";
            System.out.println(json); // TODO testing purpose, remove it later
        } catch (UserNotExistException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json); // TODO testing purpose, remove it later
        }


    }
}
