package com.hotel.management.payment;

public class Payment {
    private Integer paymentId;
    private Integer bookingId;
    private Integer facilityBookingId;
    private Integer customerId;
    private String paymentMethod;
    private Double amountPaid;
    private String paymentDate;
    private String transactionId;
    private String status;
    private String paymentReceipt;
    private String notes;

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

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

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentReceipt() { return paymentReceipt; }
    public void setPaymentReceipt(String paymentReceipt) { this.paymentReceipt = paymentReceipt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}