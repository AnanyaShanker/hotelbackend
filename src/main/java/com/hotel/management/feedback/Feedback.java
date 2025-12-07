package com.hotel.management.feedback;

import java.time.LocalDateTime;

public class Feedback {

    private Integer feedbackId;
    private Integer customerId;         // references users.user_id
    private Integer bookingId;          // nullable
    private Integer facilityBookingId;  // nullable
    private Integer rating;             // 1–5
    private String comments;
    private LocalDateTime submissionDate;

    public Feedback() {}

    public Feedback(Integer feedbackId, Integer customerId, Integer bookingId,
                    Integer facilityBookingId, Integer rating,
                    String comments, LocalDateTime submissionDate) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.bookingId = bookingId;
        this.facilityBookingId = facilityBookingId;
        this.rating = rating;
        this.comments = comments;
        this.submissionDate = submissionDate;
    }

    public Integer getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getFacilityBookingId() { return facilityBookingId; }
    public void setFacilityBookingId(Integer facilityBookingId) {
        this.facilityBookingId = facilityBookingId;
    }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}
