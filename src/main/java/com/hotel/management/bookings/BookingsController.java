package com.hotel.management.bookings;

import com.hotel.management.payment.Payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingsController {
	@Autowired
    private BookingsService bookingsService;

    
    @PostMapping
    public ResponseEntity<Bookings> createBooking(@RequestBody Bookings booking,
                                                  @RequestParam int branchId,
                                                  @RequestParam int typeId) {
        Bookings saved = bookingsService.createBooking(booking, branchId, typeId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Bookings>> getAllBookings() {
        return ResponseEntity.ok(bookingsService.getAllBookings());
    }

    @GetMapping("/branch/{branchId}/detailed")
    public ResponseEntity<List<ManagerBookingViewDTO>> getDetailedBookingsForBranch(@PathVariable int branchId) {
        List<ManagerBookingViewDTO> list = bookingsService.getDetailedBookingsForBranch(branchId);
        return ResponseEntity.ok(list);
    }
   
    @GetMapping("/{bookingId}")
    public ResponseEntity<Bookings> getBookingById(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingsService.getBookingById(bookingId));
    }

    
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Bookings>> getBookingsByBranch(@PathVariable int branchId) {
        return ResponseEntity.ok(bookingsService.getBookingsByBranch(branchId));
    }

    // 1. GET /api/bookings/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Bookings>> getBookingsByCustomer(@PathVariable int customerId) {
        List<Bookings> bookings = bookingsService.getBookingsByCustomer(customerId);
        return ResponseEntity.ok(bookings);
    }

    // 2. PATCH /api/bookings/{id}/payment-status
    @PatchMapping("/{bookingId}/payment-status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable int bookingId,
            @RequestBody java.util.Map<String, String> body) {
        String status = body.get("paymentStatus");
        bookingsService.updatePaymentStatus(bookingId, status);
        return ResponseEntity.ok("Payment status updated successfully");
    }

    // 3. GET /api/bookings/{id}/details
    @GetMapping("/{bookingId}/details")
    public ResponseEntity<BookingsDTO> getBookingDetails(@PathVariable int bookingId) {
        BookingsDTO details = bookingsService.getBookingDetails(bookingId);
        return ResponseEntity.ok(details);
    }


    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable int bookingId) {
        bookingsService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable int bookingId) {
        bookingsService.completeBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateBookingStatusBasedOnPayment(@RequestBody Payment payment) {
        bookingsService.updateBookingStatusBasedOnPayment(payment);
        return ResponseEntity.ok().build();
    }
}
