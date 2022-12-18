package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.repository.SecurityRepositoryInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@WebServlet(name = "SecurityServlet", value = "/securities/*")
public class SecurityServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(SecurityServlet.class);

    private SecurityRepositoryInterface securityRepository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        securityRepository = (SecurityRepositoryInterface) context.getAttribute("securityRepository");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        int readChars;
        try (Reader reader = request.getReader()) {
            while ((readChars = reader.read(buffer)) != -1) {
                body.append(buffer, 0, readChars);
            }
        }


        String orderStr = body.toString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Security security = mapper.readValue(orderStr, Security.class);

        try {
            securityRepository.addSecurity(security);
            response.setStatus(202);
        } catch (SecurityCreationException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);

        Long securityId = Long.parseLong(request.getPathInfo().substring(1));

        try {
            Security security = securityRepository.getSecurity(securityId);
            response.setStatus(202);
            String json = "{\n";
            json += "\"id\": " + JSONObject.quote(String.valueOf(security.getId())) + ",\n";
            json += "\"name\": " + JSONObject.quote(security.getName()) + "\n";
            json += "}";
            System.out.println(json);
        } catch (SecurityNotExistException ex) {
            String json = "{\n";
            json += "\"errorMessage\": " + JSONObject.quote(ex.getMessage()) + "\n";
            json += "}";
            response.setStatus(400);
            response.getWriter().println(json);
            System.out.println(json);
        }
    }
}
