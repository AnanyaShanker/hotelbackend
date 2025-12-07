package com.hotel.management.feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {

    Integer addFeedback(Feedback feedback);

    Optional<Feedback> getById(Integer id);

    List<Feedback> getAll();

    void updateFeedback(Integer id, Feedback feedback);

    void deleteFeedback(Integer id);

    List<Feedback> getByCustomer(Integer customerId);

    List<Feedback> getByBooking(Integer bookingId);

    List<Feedback> getByFacilityBooking(Integer facilityBookingId);

    // Admin table
    List<FeedbackDisplayDto> getAdminDisplayList();
}
