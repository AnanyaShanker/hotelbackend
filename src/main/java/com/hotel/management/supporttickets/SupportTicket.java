package com.hotel.management.supporttickets;

import java.time.LocalDateTime;

public class SupportTicket {

	private Integer ticketId;
	private Integer customerId;
	private Integer assignedStaffId;
	private Integer bookingId;
	private Integer facilityBookingId;
	private String subject;
	private String category;
	private String status; // OPEN, IN_PROGRESS, CLOSED
	private String details;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public SupportTicket() {
	}

	public SupportTicket(Integer ticketId, Integer customerId, Integer assignedStaffId, Integer bookingId,
			Integer facilityBookingId, String subject, String category, String status, String details,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.assignedStaffId = assignedStaffId;
		this.bookingId = bookingId;
		this.facilityBookingId = facilityBookingId;
		this.subject = subject;
		this.category = category;
		this.status = status;
		this.details = details;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAssignedStaffId() {
		return assignedStaffId;
	}

	public void setAssignedStaffId(Integer assignedStaffId) {
		this.assignedStaffId = assignedStaffId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getFacilityBookingId() {
		return facilityBookingId;
	}

	public void setFacilityBookingId(Integer facilityBookingId) {
		this.facilityBookingId = facilityBookingId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}