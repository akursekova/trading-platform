package dev.akursekova.servlet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.entities.Security;
import dev.akursekova.repository.SecurityRepositoryInterface;
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
import java.util.stream.Collectors;

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

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        String orderStr = request.getReader().lines().collect(Collectors.joining());
        Security security = objectMapper.readValue(orderStr, Security.class);

        securityRepository.addSecurity(security);
        response.setStatus(202);
        String securityAsJson = objectMapper.writeValueAsString(security);
        response.getWriter().println(securityAsJson);

        LOG.debug("Security with id = " + security.getId() + " successfully created: " + "\n" + securityAsJson);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Long securityId = Long.parseLong(request.getPathInfo().substring(1));

        Security security = securityRepository.getSecurity(securityId);
        response.setStatus(202);

        String securityAsJson = objectMapper.writeValueAsString(security);
        response.getWriter().println(securityAsJson);

        LOG.debug("Requested Security with id = " + securityId + ": " + "\n" + securityAsJson);
    }
}
