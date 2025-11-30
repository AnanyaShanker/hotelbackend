package com.hotel.management.payment;

import jakarta.validation.constraints.*;

public class PaymentRequest {

    private Integer bookingId;
    private Integer facilityBookingId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be >= 0")
    private Double amountPaid;

    private String notes;

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getFacilityBookingId() { return facilityBookingId; }
    public void setFacilityBookingId(Integer facilityBookingId) { this.facilityBookingId = facilityBookingId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}