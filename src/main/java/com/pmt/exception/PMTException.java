package com.pmt.exception;

/**
 * Custom Exception for Application-specific errors
 * Follows OOP abstraction principle
 */
public class PMTException extends Exception {
    private static final long serialVersionUID = 1L;
    protected String errorCode;
    protected int statusCode;

    public PMTException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public PMTException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public PMTException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = 500;
    }

    public PMTException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

/**
 * Custom Exception for Database operations
 */
class DatabaseException extends PMTException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message, "DB_ERROR", 500);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "DB_ERROR";
        this.statusCode = 500;
    }
}

/**
 * Custom Exception for Authentication failures
 */
class AuthenticationException extends PMTException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message, "AUTH_ERROR", 401);
    }
}

/**
 * Custom Exception for Authorization failures
 */
class AuthorizationException extends PMTException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message, "AUTHZ_ERROR", 403);
    }
}

/**
 * Custom Exception for Resource not found
 */
class ResourceNotFoundException extends PMTException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message, "NOT_FOUND", 404);
    }
}

/**
 * Custom Exception for Validation errors
 */
class ValidationException extends PMTException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", 400);
    }
}
