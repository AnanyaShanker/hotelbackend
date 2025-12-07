package com.hotel.management.exception;

/**
 * Exception thrown for invalid input data
 */
public class InvalidInputException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
    private Object rejectedValue;
    private String validationMessage;

    public InvalidInputException(String fieldName, Object rejectedValue, String validationMessage) {
        super(String.format("Invalid input for field '%s' with value '%s': %s",
            fieldName, rejectedValue, validationMessage));
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.validationMessage = validationMessage;
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
}

