package com.qquique.jamm.api.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qquique.jamm.api.controller.exception.ControllerException;
import com.qquique.jamm.application.service.exception.ServiceException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GlobalExceptionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionFilter.class);
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            logger.info("GlobalExceptionFilter called");
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Handle exceptions here
            logger.info(e);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(getStatusCode(e));
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(createErrorResponse(e));
        }
    }

    private int getStatusCode(Exception e) {
        if (e instanceof ControllerException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (e instanceof ServiceException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    private String createErrorResponse(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (e instanceof ControllerException) {
            errorResponse.setErrorCode("INTERNAL_SERVER_ERROR");
        } else if (e instanceof ServiceException) {
            errorResponse.setErrorCode("INTERNAL_SERVER_ERROR");
        }
        errorResponse.setErrorMessage("Server Error");
        try {
            return objectMapper.writeValueAsString(errorResponse);
        } catch (JsonProcessingException ex) {
            // Handle JSON serialization errors
            return "{\"errorCode\": \"JSON_ERROR\", \"errorMessage\": \"Error generating error response\"}";
        }
    }
}

