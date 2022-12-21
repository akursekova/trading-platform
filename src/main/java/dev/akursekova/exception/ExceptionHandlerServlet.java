package dev.akursekova.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.akursekova.dto.CreatedUserDto;
import dev.akursekova.dto.ErrorMessageDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ExceptionHandlerServlet", value = "/exception_handler")
public class ExceptionHandlerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        ErrorMessageDto errorMessageDto = ErrorMessageDto.builder()
                .errorMessage(throwable.getMessage())
                .build();
        String errorAsJson = objectMapper.writeValueAsString(errorMessageDto);

        response.setStatus(400);
        response.getWriter().println(errorAsJson);
    }
}
