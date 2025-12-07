package com.hotel.management.bookings;

import com.hotel.management.payment.Payment;
import java.util.List;

public interface BookingsService {
    Bookings createBooking(Bookings booking, int branchId, int typeId);
    Bookings getBookingById(int bookingId);
    List<Bookings> getAllBookings();
    List<Bookings> getBookingsByBranch(int branchId);
    List<Bookings> getBookingsByCustomer(int customerId);
    void updatePaymentStatus(int bookingId, String paymentStatus);
    BookingsDTO getBookingDetails(int bookingId);
    void cancelBooking(int bookingId);
    void completeBooking(int bookingId);
    
  List<ManagerBookingViewDTO> getDetailedBookingsForBranch(int branchId);
    
    void updateBookingStatusBasedOnPayment(Payment payment);
}
