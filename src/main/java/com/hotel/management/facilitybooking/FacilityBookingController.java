
package com.hotel.management.facilitybooking;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/facility-bookings")
public class FacilityBookingController {

    private final FacilityBookingService bookingService;

    public FacilityBookingController(FacilityBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody FacilityBookingRequest req) {
        try {
            FacilityBooking created = bookingService.createBooking(req);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ia) {
            return ResponseEntity.badRequest().body(ia.getMessage());
        } catch (IllegalStateException ie) {
            return ResponseEntity.status(409).body(ie.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Booking failed: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            System.out.println(" Fetching basic facility booking for ID: " + id);
            FacilityBooking b = bookingService.getBookingById(id);
            if (b == null) {
                System.err.println(" Facility booking not found with ID: " + id);
                return ResponseEntity.status(404).body("Booking not found with ID: " + id);
            }
            System.out.println(" Successfully fetched basic facility booking for ID: " + id);
            return ResponseEntity.ok(b);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.err.println(" EmptyResultDataAccessException for booking ID: " + id);
            return ResponseEntity.status(404).body("Booking not found with ID: " + id);
        } catch (Exception e) {
            System.err.println(" Exception fetching booking for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(404).body("Booking not found");
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getFullById(@PathVariable int id) {
        try {
            System.out.println(" Fetching facility booking details for ID: " + id);
            FacilityBookingFullDTO dto = bookingService.getFullBookingById(id);
            if (dto == null) {
                System.err.println(" Facility booking not found with ID: " + id);
                return ResponseEntity.status(404).body("Booking not found with ID: " + id);
            }
            System.out.println(" Successfully fetched facility booking details for ID: " + id);
            return ResponseEntity.ok(dto);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.err.println(" EmptyResultDataAccessException for booking ID: " + id);
            return ResponseEntity.status(404).body("Booking not found with ID: " + id);
        } catch (Exception e) {
            System.err.println(" Exception fetching booking details for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching booking details: " + e.getMessage());
        }
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<?> listByFacility(@PathVariable int facilityId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<FacilityBooking> list = bookingService.getBookingsForFacility(facilityId, date);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to list bookings: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> listByCustomer(@PathVariable int customerId) {
        try {
            List<FacilityBooking> list = bookingService.getBookingsByCustomer(customerId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to list bookings: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable int id, @RequestParam(required = false) Integer requestedBy) {
        try {
            boolean success = bookingService.cancelBooking(id, requestedBy == null ? -1 : requestedBy);
            return ResponseEntity.ok(java.util.Map.of(
                "success", success,
                "message", success ? "Booking cancelled successfully" : "Failed to cancel booking"
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Cancel failed: " + e.getMessage()));
        }
    }

    // 4. PATCH /facility-bookings/{id}/payment-status
    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable int id,
            @RequestBody java.util.Map<String, String> body) {
        try {
            String status = body.get("paymentStatus");
            bookingService.updatePaymentStatus(id, status);
            return ResponseEntity.ok("Payment status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }
}
