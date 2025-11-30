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

   
    @GetMapping("/{bookingId}")
    public ResponseEntity<Bookings> getBookingById(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingsService.getBookingById(bookingId));
    }

    
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Bookings>> getBookingsByBranch(@PathVariable int branchId) {
        return ResponseEntity.ok(bookingsService.getBookingsByBranch(branchId));
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
