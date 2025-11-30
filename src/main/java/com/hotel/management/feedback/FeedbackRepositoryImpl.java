package com.hotel.management.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void addFeedback(Feedback feedback) {
		String sql = """
				INSERT INTO feedback (
				    customer_id, booking_id, facility_booking_id,
				    rating, comments, feedback_image, submission_date
				) VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		jdbcTemplate.update(sql, feedback.getCustomerId(), feedback.getBookingId(), feedback.getFacilityBookingId(),
				feedback.getRating(), feedback.getComments(), feedback.getFeedbackImage(),
				Timestamp.valueOf(LocalDateTime.now()));
	}

	@Override
	public Feedback getFeedbackById(Integer feedbackId) {
		String sql = "SELECT * FROM feedback WHERE feedback_id = ?";

		List<Feedback> list = jdbcTemplate.query(sql, new FeedbackRowMapper(), feedbackId);

		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<Feedback> getAllFeedback() {
		String sql = "SELECT * FROM feedback ORDER BY submission_date DESC";
		return jdbcTemplate.query(sql, new FeedbackRowMapper());
	}

	@Override
	public List<Feedback> getFeedbackByCustomer(Integer customerId) {
		String sql = "SELECT * FROM feedback WHERE customer_id = ? ORDER BY submission_date DESC";
		return jdbcTemplate.query(sql, new FeedbackRowMapper(), customerId);
	}

	@Override
	public List<Feedback> getFeedbackByBooking(Integer bookingId) {
		String sql = "SELECT * FROM feedback WHERE booking_id = ? ORDER BY submission_date DESC";
		return jdbcTemplate.query(sql, new FeedbackRowMapper(), bookingId);
	}

	@Override
	public List<Feedback> getFeedbackByFacilityBooking(Integer facilityBookingId) {
		String sql = "SELECT * FROM feedback WHERE facility_booking_id = ? ORDER BY submission_date DESC";
		return jdbcTemplate.query(sql, new FeedbackRowMapper(), facilityBookingId);
	}

	@Override
	public void updateFeedback(Integer feedbackId, Feedback feedback) {
		String sql = """
				UPDATE feedback SET
				    rating = ?,
				    comments = ?,
				    feedback_image = ?
				WHERE feedback_id = ?
				""";

		jdbcTemplate.update(sql, feedback.getRating(), feedback.getComments(), feedback.getFeedbackImage(), feedbackId);
	}

	@Override
	public void deleteFeedback(Integer feedbackId) {
		String sql = "DELETE FROM feedback WHERE feedback_id = ?";
		jdbcTemplate.update(sql, feedbackId);
	}
}
