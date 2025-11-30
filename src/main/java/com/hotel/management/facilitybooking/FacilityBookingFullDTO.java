package com.hotel.management.facilitybooking;


public class FacilityBookingFullDTO {
    // booking fields
    private int facilityBookingId;
    private int customerId;
    private int facilityId;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String timeSlot;
    private int quantity;
    private double totalPrice;
    private String paymentStatus;
    private String bookingStatus;
    private String notes;
    private String createdAt;
    private String updatedAt;

    // user info
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // facility info
    private String facilityName;
    private String facilityType;
    private String facilityLocation;
    private Integer facilityCapacity;

    // payment summary (nullable)
    private Integer paymentId;
    private String paymentStatusDetail;
    private Double paymentAmount;

    public FacilityBookingFullDTO() {}

	public int getFacilityBookingId() {
		return facilityBookingId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public String getNotes() {
		return notes;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public String getFacilityType() {
		return facilityType;
	}

	public String getFacilityLocation() {
		return facilityLocation;
	}

	public Integer getFacilityCapacity() {
		return facilityCapacity;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public String getPaymentStatusDetail() {
		return paymentStatusDetail;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setFacilityBookingId(int facilityBookingId) {
		this.facilityBookingId = facilityBookingId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}

	public void setFacilityLocation(String facilityLocation) {
		this.facilityLocation = facilityLocation;
	}

	public void setFacilityCapacity(Integer facilityCapacity) {
		this.facilityCapacity = facilityCapacity;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public void setPaymentStatusDetail(String paymentStatusDetail) {
		this.paymentStatusDetail = paymentStatusDetail;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

   
}

