package com.qquique.jamm.api.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class JsonRequestFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(JsonRequestFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("JsonRequestFilter called");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contentType = httpRequest.getContentType();
        if (contentType != null && contentType.equals("application/json")) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Unsupported Media Type. Only application/json is supported.");
        }
    }
}

