package com.hotel.management.exception;

/**
 * Exception thrown for booking-related failures
 */
public class BookingException extends RuntimeException {

    private String bookingId;
    private String errorCode;
    private String reason;

    public BookingException(String bookingId, String errorCode, String reason) {
        super(String.format("Booking failed for ID '%s' [%s]: %s", bookingId, errorCode, reason));
        this.bookingId = bookingId;
        this.errorCode = errorCode;
        this.reason = reason;
    }

    public BookingException(String message) {
        super(message);
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }
}

