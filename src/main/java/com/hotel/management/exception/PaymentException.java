package com.hotel.management.exception;

/**
 * Exception thrown for payment processing failures
 */
public class PaymentException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String paymentId;
    private String errorCode;
    private String reason;

    public PaymentException(String paymentId, String errorCode, String reason) {
        super(String.format("Payment failed for ID '%s' [%s]: %s", paymentId, errorCode, reason));
        this.paymentId = paymentId;
        this.errorCode = errorCode;
        this.reason = reason;
    }

    public PaymentException(String message) {
        super(message);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }
}

	