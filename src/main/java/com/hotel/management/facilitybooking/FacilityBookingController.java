
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
            FacilityBooking b = bookingService.getBookingById(id);
            return ResponseEntity.ok(b);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Booking not found");
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getFullById(@PathVariable int id) {
        try {
            FacilityBookingFullDTO dto = bookingService.getFullBookingById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Booking not found");
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
            boolean ok = bookingService.cancelBooking(id, requestedBy == null ? -1 : requestedBy);
            return ResponseEntity.ok(ok ? "Cancelled" : "Cancel failed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Cancel failed: " + e.getMessage());
        }
    }
}
