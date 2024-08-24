package com.qquique.jamm.infrastructure.database.exception;

public class RepositoryException extends RuntimeException {

    public RepositoryException(String message, Exception e) {
        super(message, e);
    }
}
