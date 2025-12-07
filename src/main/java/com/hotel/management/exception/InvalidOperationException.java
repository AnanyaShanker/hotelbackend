package com.hotel.management.exception;

/**
 * Exception thrown for invalid business operations
 */
public class InvalidOperationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operation;
    private String reason;

    public InvalidOperationException(String operation, String reason) {
        super(String.format("Invalid operation '%s': %s", operation, reason));
        this.operation = operation;
        this.reason = reason;
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    public String getOperation() {
        return operation;
    }

    public String getReason() {
        return reason;
    }
}

