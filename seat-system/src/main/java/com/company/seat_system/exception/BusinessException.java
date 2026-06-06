package com.company.seat_system.exception;

/**
 * Custom exception for business logic errors in the seat system.
 * Used to indicate validation or business rule violations.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructs a BusinessException with a message.
     *
     * @param message the error message describing the business exception
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructs a BusinessException with a message and cause.
     *
     * @param message the error message describing the business exception
     * @param cause   the underlying exception that caused this error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
