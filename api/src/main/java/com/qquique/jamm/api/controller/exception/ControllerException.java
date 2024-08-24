package com.qquique.jamm.api.controller.exception;

public class ControllerException extends RuntimeException {
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}

