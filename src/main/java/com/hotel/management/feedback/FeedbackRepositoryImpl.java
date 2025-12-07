package com.hotel.management.feedback;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final FeedbackRowMapper feedbackRowMapper = new FeedbackRowMapper();
    private final FeedbackDisplayRowMapper displayRowMapper = new FeedbackDisplayRowMapper();

    public FeedbackRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("feedback")
                .usingGeneratedKeyColumns("feedback_id");
    }

    @Override
    public Integer addFeedback(Feedback feedback) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", feedback.getCustomerId());
        params.put("booking_id", feedback.getBookingId());
        params.put("facility_booking_id", feedback.getFacilityBookingId());
        params.put("rating", feedback.getRating());
        params.put("comments", feedback.getComments());
        // submission_date column should have DEFAULT CURRENT_TIMESTAMP in DB

        Number key = insert.executeAndReturnKey(params);
        return key.intValue();
    }

    @Override
    public Optional<Feedback> getById(Integer id) {
        try {
            Feedback fb = jdbcTemplate.queryForObject(
                    "SELECT * FROM feedback WHERE feedback_id = ?",
                    feedbackRowMapper,
                    id
            );
            return Optional.ofNullable(fb);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Feedback> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM feedback ORDER BY submission_date DESC",
                feedbackRowMapper
        );
    }

    @Override
    public void updateFeedback(Integer id, Feedback feedback) {
        jdbcTemplate.update(
                "UPDATE feedback SET rating = ?, comments = ? WHERE feedback_id = ?",
                feedback.getRating(),
                feedback.getComments(),
                id
        );
    }

    @Override
    public void deleteFeedback(Integer id) {
        jdbcTemplate.update("DELETE FROM feedback WHERE feedback_id = ?", id);
    }

    @Override
    public List<Feedback> getByCustomer(Integer customerId) {
        return jdbcTemplate.query(
                "SELECT * FROM feedback WHERE customer_id = ? ORDER BY submission_date DESC",
                feedbackRowMapper,
                customerId
        );
    }

    @Override
    public List<Feedback> getByBooking(Integer bookingId) {
        return jdbcTemplate.query(
                "SELECT * FROM feedback WHERE booking_id = ? ORDER BY submission_date DESC",
                feedbackRowMapper,
                bookingId
        );
    }

    @Override
    public List<Feedback> getByFacilityBooking(Integer facilityBookingId) {
        return jdbcTemplate.query(
                "SELECT * FROM feedback WHERE facility_booking_id = ? ORDER BY submission_date DESC",
                feedbackRowMapper,
                facilityBookingId
        );
    }

    // -------------------------------------------------
    // Admin view – single SQL with LEFT JOINs
    // -------------------------------------------------
    private static final String ADMIN_DISPLAY_SQL = """
        SELECT
            f.feedback_id,
            u.name AS customer_name,
            CASE
                WHEN f.booking_id IS NOT NULL
                    THEN CONCAT('Room ', r.room_number)
                WHEN f.facility_booking_id IS NOT NULL
                    THEN fac.name
                ELSE 'N/A'
            END AS item_name,
            f.rating,
            f.comments
        FROM feedback f
        JOIN users u
            ON u.user_id = f.customer_id
        LEFT JOIN bookings b
            ON b.booking_id = f.booking_id
        LEFT JOIN rooms r
            ON r.room_id = b.room_id
        LEFT JOIN facility_bookings fb
            ON fb.facility_booking_id = f.facility_booking_id
        LEFT JOIN facilities fac
            ON fac.facility_id = fb.facility_id
        ORDER BY f.feedback_id DESC
        """;

    @Override
    public List<FeedbackDisplayDto> getAdminDisplayList() {
        return jdbcTemplate.query(ADMIN_DISPLAY_SQL, displayRowMapper);
    }
}
