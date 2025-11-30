package com.hotel.management.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	// CREATE
	@PostMapping
	public FeedbackDto addFeedback(@RequestBody FeedbackDto dto) {
		return feedbackService.addFeedback(dto);
	}

	// GET ALL
	@GetMapping
	public List<FeedbackDto> getAllFeedback() {
		return feedbackService.getAllFeedback();
	}

	// GET BY ID
	@GetMapping("/{id}")
	public FeedbackDto getFeedbackById(@PathVariable Integer id) {
		return feedbackService.getFeedbackById(id);
	}

	// GET BY CUSTOMER
	@GetMapping("/customer/{customerId}")
	public List<FeedbackDto> getFeedbackByCustomer(@PathVariable Integer customerId) {
		return feedbackService.getFeedbackByCustomer(customerId);
	}

	// GET BY BOOKING
	@GetMapping("/booking/{bookingId}")
	public List<FeedbackDto> getFeedbackByBooking(@PathVariable Integer bookingId) {
		return feedbackService.getFeedbackByBooking(bookingId);
	}

	// GET BY FACILITY BOOKING
	@GetMapping("/facility/{facilityBookingId}")
	public List<FeedbackDto> getFeedbackByFacilityBooking(@PathVariable Integer facilityBookingId) {
		return feedbackService.getFeedbackByFacilityBooking(facilityBookingId);
	}

	// UPDATE
	@PutMapping("/{id}")
	public FeedbackDto updateFeedback(@PathVariable Integer id, @RequestBody FeedbackDto dto) {
		return feedbackService.updateFeedback(id, dto);
	}

	// DELETE
	@DeleteMapping("/{id}")
	public String deleteFeedback(@PathVariable Integer id) {
		feedbackService.deleteFeedback(id);
		return "Feedback deleted successfully.";
	}
}
