package com.hotel.management.facilitybooking;

public class FacilityBookingRequest {
    private int customerId;
    private int facilityId;
    private String bookingDate; // yyyy-MM-dd
    private String startTime; // HH:mm
    private String endTime;   // HH:mm
    private String timeSlot;
    private int quantity = 1;
    private String notes;
    private boolean autoCapturePayment = false;

    public FacilityBookingRequest() {}

    // getters & setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getFacilityId() { return facilityId; }
    public void setFacilityId(int facilityId) { this.facilityId = facilityId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isAutoCapturePayment() { return autoCapturePayment; }
    public void setAutoCapturePayment(boolean autoCapturePayment) { this.autoCapturePayment = autoCapturePayment; }
}

