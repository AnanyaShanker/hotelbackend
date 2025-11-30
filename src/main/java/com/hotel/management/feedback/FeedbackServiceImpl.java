package com.hotel.management.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public FeedbackDto addFeedback(FeedbackDto dto) {

		validateBookingRelationship(dto);

		Feedback feedback = convertToEntity(dto);

		feedbackRepository.addFeedback(feedback);

		return dto;
	}

	@Override
	public FeedbackDto getFeedbackById(Integer feedbackId) {

		Feedback feedback = feedbackRepository.getFeedbackById(feedbackId);

		if (feedback == null) {
			throw new RuntimeException("Feedback not found with ID " + feedbackId);
		}

		return convertToDto(feedback);
	}

	@Override
	public List<FeedbackDto> getAllFeedback() {

		return feedbackRepository.getAllFeedback().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public List<FeedbackDto> getFeedbackByCustomer(Integer customerId) {

		return feedbackRepository.getFeedbackByCustomer(customerId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<FeedbackDto> getFeedbackByBooking(Integer bookingId) {

		return feedbackRepository.getFeedbackByBooking(bookingId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<FeedbackDto> getFeedbackByFacilityBooking(Integer facilityBookingId) {

		return feedbackRepository.getFeedbackByFacilityBooking(facilityBookingId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public FeedbackDto updateFeedback(Integer feedbackId, FeedbackDto dto) {

		Feedback existing = feedbackRepository.getFeedbackById(feedbackId);

		if (existing == null) {
			throw new RuntimeException("Feedback not found with ID " + feedbackId);
		}

		validateBookingRelationship(dto);

		Feedback updated = convertToEntity(dto);
		updated.setFeedbackId(feedbackId);

		feedbackRepository.updateFeedback(feedbackId, updated);

		return convertToDto(updated);
	}

	@Override
	public void deleteFeedback(Integer feedbackId) {

		Feedback existing = feedbackRepository.getFeedbackById(feedbackId);

		if (existing == null) {
			throw new RuntimeException("Feedback not found with ID " + feedbackId);
		}

		feedbackRepository.deleteFeedback(feedbackId);
	}

	/*
	 * -------------------------- Utility Methods ---------------------------
	 */
	private Feedback convertToEntity(FeedbackDto dto) {

		return new Feedback(dto.getFeedbackId(), dto.getCustomerId(), dto.getBookingId(), dto.getFacilityBookingId(),
				dto.getRating(), dto.getComments(), dto.getFeedbackImage(), dto.getSubmissionDate());
	}

	private FeedbackDto convertToDto(Feedback entity) {

		return new FeedbackDto(entity.getFeedbackId(), entity.getCustomerId(), entity.getBookingId(),
				entity.getFacilityBookingId(), entity.getRating(), entity.getComments(), entity.getFeedbackImage(),
				entity.getSubmissionDate());
	}

	private void validateBookingRelationship(FeedbackDto dto) {

		// Rule: either booking or facility booking should be filled, not both
		if (dto.getBookingId() != null && dto.getFacilityBookingId() != null) {
			throw new RuntimeException("Feedback cannot have both bookingId and facilityBookingId.");
		}

		if (dto.getBookingId() == null && dto.getFacilityBookingId() == null) {
			throw new RuntimeException("Feedback must be linked to either a booking or facility booking.");
		}
	}
}
