package com.hotel.management.feedback;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createFeedback(FeedbackDto dto);

    FeedbackDto getById(Integer id);

    List<FeedbackDto> getAll();

    List<FeedbackDto> getByCustomer(Integer customerId);

    List<FeedbackDto> getByBooking(Integer bookingId);

    List<FeedbackDto> getByFacilityBooking(Integer facilityBookingId);

    FeedbackDto update(Integer id, FeedbackDto dto);

    void delete(Integer id);

    // Admin table
    List<FeedbackDisplayDto> getAdminDisplayList();
}