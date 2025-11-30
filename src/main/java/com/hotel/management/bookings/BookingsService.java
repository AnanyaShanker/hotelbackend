package com.hotel.management.bookings;

import com.hotel.management.payment.Payment;
import java.util.List;

public interface BookingsService {
    Bookings createBooking(Bookings booking, int branchId, int typeId);
    Bookings getBookingById(int bookingId);
    List<Bookings> getAllBookings();
    List<Bookings> getBookingsByBranch(int branchId);
    void cancelBooking(int bookingId);
    void completeBooking(int bookingId);
    void updateBookingStatusBasedOnPayment(Payment payment);
}
