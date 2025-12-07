package com.hotel.management.exception;

/**
 * Exception thrown for unauthorized access attempts
 */
public class UnauthorizedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resource;
    private String action;

    public UnauthorizedException(String resource, String action) {
        super(String.format("Unauthorized to %s %s", action, resource));
        this.resource = resource;
        this.action = action;
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }
}

	