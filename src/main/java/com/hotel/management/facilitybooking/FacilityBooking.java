
package com.hotel.management.facilitybooking;

public class FacilityBooking {
    private int facilityBookingId;
    private int customerId;
    private int facilityId;
    private String bookingDate;   // yyyy-MM-dd
    private String startTime;     // HH:mm or null
    private String endTime;       // HH:mm or null
    private String timeSlot;      // optional descriptive
    private int quantity;
    private double totalPrice;
    private String paymentStatus; // PENDING, SUCCESS, FAILED
    private String bookingStatus; // CONFIRMED, CANCELLED, COMPLETED
    private String notes;
    private String createdAt;
    private String updatedAt;

    public FacilityBooking() {}

    // getters & setters
    public int getFacilityBookingId() { return facilityBookingId; }
    public void setFacilityBookingId(int facilityBookingId) { this.facilityBookingId = facilityBookingId; }

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

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
