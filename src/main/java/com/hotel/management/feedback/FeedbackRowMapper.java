package com.hotel.management.feedback;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FeedbackRowMapper implements RowMapper<Feedback> {

	@Override
	public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {

		Feedback feedback = new Feedback();

		feedback.setFeedbackId(rs.getInt("feedback_id"));
		feedback.setCustomerId(rs.getInt("customer_id"));

		// Nullable values
		Object bookingIdObj = rs.getObject("booking_id");
		feedback.setBookingId(bookingIdObj != null ? (Integer) bookingIdObj : null);

		Object facilityBookingIdObj = rs.getObject("facility_booking_id");
		feedback.setFacilityBookingId(facilityBookingIdObj != null ? (Integer) facilityBookingIdObj : null);

		feedback.setRating(rs.getInt("rating"));
		feedback.setComments(rs.getString("comments"));
		feedback.setFeedbackImage(rs.getString("feedback_image"));

		// Convert SQL timestamp to LocalDateTime
		if (rs.getTimestamp("submission_date") != null) {
			feedback.setSubmissionDate(rs.getTimestamp("submission_date").toLocalDateTime());
		}

		return feedback;
	}
}
