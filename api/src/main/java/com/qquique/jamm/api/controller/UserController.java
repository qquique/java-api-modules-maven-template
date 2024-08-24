package com.qquique.jamm.api.controller;

import com.qquique.jamm.api.controller.exception.ControllerException;
import com.qquique.jamm.api.filter.ErrorResponse;
import com.qquique.jamm.api.util.ErrorMessages;
import com.qquique.jamm.application.dto.UserDTO;
import com.qquique.jamm.application.service.UserService;
import com.qquique.jamm.application.service.exception.ServiceException;
import com.qquique.jamm.infrastructure.database.Database;
import com.qquique.jamm.infrastructure.database.repository.UserRepositoryImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

@WebServlet("/api/user/*")
public class UserController extends HttpServlet {
    private enum MethodError {
        ON_GET,
        ON_POST,
        ON_PUT,
        ON_DELETE
    }
    private UserService userService;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if (userService == null) {
            userService = createUserService();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<UserDTO> userDTOList = userService.getAllUsers();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(userDTOList));
        } else {
            Long id = Long.parseLong(pathInfo.substring(1));
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO != null) {
                response.getWriter().write(objectMapper.writeValueAsString(userDTO));
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
        if (userService == null) {
            userService = createUserService();
        }
        UserDTO userDTO = objectMapper.readValue(request.getReader(), UserDTO.class);
        userDTO = userService.createUser(userDTO);

        if (userDTO != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(userDTO));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(objectMapper.writeValueAsString(createErrorResponse(MethodError.ON_POST)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        if (userService == null) {
            userService = createUserService();
        }

        UserDTO userDTO = objectMapper.readValue(request.getReader(), UserDTO.class);
        try {
            userDTO = userService.updateUser(userDTO.getId(), userDTO);

            if (userDTO != null) {
                response.getWriter().write(objectMapper.writeValueAsString(userDTO));
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
        if (userService == null) {
            userService = createUserService();
        }
        try {
            Long id = Long.parseLong(request.getPathInfo().substring(1));
            userService.deleteUser(id);
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

    private UserService createUserService() {
        UserService newUserService;
        try {
            newUserService = new UserService(new UserRepositoryImpl(Database.createWithXmlConfiguration().getSessionFactory()));
        } catch (Throwable e) {
            String msg = ErrorMessages.getErrorMessage("controller.error_initializing_service");
            logger.error(msg, e);
            throw new ControllerException(msg, e);
        }
        return newUserService;
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

