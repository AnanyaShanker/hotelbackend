package com.hotel.management.feedback;

public class FeedbackSummaryDto {

	private Integer feedbackId;
	private String customerName;
	private String branchName;
	private String itemName; // "Room 101" or "Luxury Spa" etc.
	private Integer bookingOrFacilityId; // booking_id OR facility_booking_id
	private Integer rating;
	private String comments;

	public FeedbackSummaryDto() {
	}

	public FeedbackSummaryDto(Integer feedbackId, String customerName, String branchName, String itemName,
			Integer bookingOrFacilityId, Integer rating, String comments) {
		this.feedbackId = feedbackId;
		this.customerName = customerName;
		this.branchName = branchName;
		this.itemName = itemName;
		this.bookingOrFacilityId = bookingOrFacilityId;
		this.rating = rating;
		this.comments = comments;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getBookingOrFacilityId() {
		return bookingOrFacilityId;
	}

	public void setBookingOrFacilityId(Integer bookingOrFacilityId) {
		this.bookingOrFacilityId = bookingOrFacilityId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}