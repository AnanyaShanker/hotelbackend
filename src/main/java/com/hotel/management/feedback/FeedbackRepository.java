package com.hotel.management.feedback;

import java.util.List;

public interface FeedbackRepository {

	// Create
	void addFeedback(Feedback feedback);

	// Read
	Feedback getFeedbackById(Integer feedbackId);

	List<Feedback> getAllFeedback();

	List<Feedback> getFeedbackByCustomer(Integer customerId);

	List<Feedback> getFeedbackByBooking(Integer bookingId);

	List<Feedback> getFeedbackByFacilityBooking(Integer facilityBookingId);

	// Update
	void updateFeedback(Integer feedbackId, Feedback feedback);

	// Delete
	void deleteFeedback(Integer feedbackId);
}
