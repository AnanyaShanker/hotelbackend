package com.hotel.management.feedback;

import java.util.List;

public interface FeedbackService {

	FeedbackDto addFeedback(FeedbackDto dto);

	FeedbackDto getFeedbackById(Integer feedbackId);

	List<FeedbackDto> getAllFeedback();

	List<FeedbackDto> getFeedbackByCustomer(Integer customerId);

	List<FeedbackDto> getFeedbackByBooking(Integer bookingId);

	List<FeedbackDto> getFeedbackByFacilityBooking(Integer facilityBookingId);

	FeedbackDto updateFeedback(Integer feedbackId, FeedbackDto dto);

	void deleteFeedback(Integer feedbackId);
}
