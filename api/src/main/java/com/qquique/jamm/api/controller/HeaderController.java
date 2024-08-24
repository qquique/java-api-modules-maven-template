package com.qquique.jamm.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qquique.jamm.api.controller.exception.ControllerException;
import com.qquique.jamm.application.dto.HeaderDTO;
import com.qquique.jamm.application.service.HeaderService;
import com.qquique.jamm.application.service.exception.ServiceException;
import com.qquique.jamm.infrastructure.database.repository.HeaderRepositoryImpl;
import com.qquique.jamm.infrastructure.database.Database;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import com.qquique.jamm.api.util.ErrorMessages;
import com.qquique.jamm.api.filter.ErrorResponse;

@WebServlet("/api/header/*")
public class HeaderController extends HttpServlet {
    private enum MethodError {
        ON_GET,
        ON_POST,
        ON_PUT,
        ON_DELETE
    }

    private static final Logger logger = LogManager.getLogger(HeaderController.class);
    private HeaderService headerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if (headerService == null) {
            headerService = createHeaderService();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<HeaderDTO> headerDTOList = headerService.getAllHeaders();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(headerDTOList));
        } else {
            Long id = Long.parseLong(pathInfo.substring(1));
            HeaderDTO headerDTO;
            headerDTO = headerService.getHeaderById(id);
            if (headerDTO != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(headerDTO));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_GET)));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        if (headerService == null) {
            headerService = createHeaderService();
        }
        HeaderDTO headerDTO = objectMapper.readValue(request.getReader(), HeaderDTO.class);
        headerDTO = headerService.createHeader(headerDTO);
        if (headerDTO != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(headerDTO));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_POST)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        if (headerService == null) {
            headerService = createHeaderService();
        }
        HeaderDTO headerDTO = objectMapper.readValue(request.getReader(), HeaderDTO.class);
        try {
            headerDTO = headerService.updateHeader(headerDTO.getId(), headerDTO);
            if (headerDTO != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(headerDTO));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_PUT)));
            }
        } catch (ServiceException e) {
            logger.error(ErrorMessages.getErrorMessage("controller.error_on_service"), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_PUT)));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        if (headerService == null) {
            headerService = createHeaderService();
        }
        try {
            Long id = Long.parseLong(request.getPathInfo().substring(1));
            headerService.deleteHeader(id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.
                    writeValueAsString(
                            createErrorResponse(
                                    ErrorMessages.getErrorMessage("controller.error_response_code"),
                                    ErrorMessages.getErrorMessage("controller.response_deletion_success")
                            )
                    ));
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_DELETE)));
        }
    }

    private HeaderService createHeaderService() {
        HeaderService newHeaderService;
        try {
            newHeaderService = new HeaderService(new HeaderRepositoryImpl(Database.createWithXmlConfiguration().getSessionFactory()));
        } catch (Throwable e) {
            String msg = ErrorMessages.getErrorMessage("controller.error_initializing_service");
            logger.error(msg, e);
            throw new ControllerException(msg, e);
        }
        return newHeaderService;
    }

    private ErrorResponse createErrorResponse(String code, String msg) {
        return new ErrorResponse(code, msg);
    }

    private ErrorResponse createErrorResponse(MethodError method) {
        String errorCode = ErrorMessages.getErrorMessage("controller.error_response_code");
        return switch (method) {
            case ON_GET ->
                    createErrorResponse(errorCode, ErrorMessages.getErrorMessage("controller.error_response_not_found"));
            case ON_POST ->
                    createErrorResponse(errorCode, ErrorMessages.getErrorMessage("controller.error_response_creation_failed"));
            case ON_PUT ->
                    createErrorResponse(errorCode, ErrorMessages.getErrorMessage("controller.error_response_update_failed"));
            case ON_DELETE ->
                    createErrorResponse(errorCode, ErrorMessages.getErrorMessage("controller.error_response_delete_failed"));
        };
    }

}

