package com.hotel.management.bookings;

import java.util.List;

public interface BookingsRepository {
    Bookings save(Bookings booking);
    List<Bookings> findAll();
    Bookings findById(int bookingId);
    void updateStatus(int bookingId, String status);
    List<Bookings> findByBranch(int branchId);
    List<Bookings> findByCustomer(int customerId);
    void updatePaymentStatus(int bookingId, String paymentStatus);
    List<ManagerBookingViewDTO> findDetailedByBranch(int branchId);
    boolean existsByIdAndCustomerId(Integer bookingId, Integer customerId);
}
