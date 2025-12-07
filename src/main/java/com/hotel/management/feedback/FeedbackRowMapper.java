package com.hotel.management.feedback;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FeedbackRowMapper implements RowMapper<Feedback> {

    @Override
    public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feedback f = new Feedback();
        f.setFeedbackId(rs.getInt("feedback_id"));
        f.setCustomerId(rs.getInt("customer_id"));

        Object bookingObj = rs.getObject("booking_id");
        f.setBookingId(bookingObj != null ? (Integer) bookingObj : null);

        Object fbObj = rs.getObject("facility_booking_id");
        f.setFacilityBookingId(fbObj != null ? (Integer) fbObj : null);

        f.setRating(rs.getInt("rating"));
        f.setComments(rs.getString("comments"));

        if (rs.getTimestamp("submission_date") != null) {
            f.setSubmissionDate(rs.getTimestamp("submission_date").toLocalDateTime());
        }
        return f;
    }
}