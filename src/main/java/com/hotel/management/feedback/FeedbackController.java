package com.hotel.management.feedback;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // ---------------- Basic CRUD ----------------

    @PostMapping
    public ResponseEntity<FeedbackDto> create(@RequestBody FeedbackDto dto) {
        FeedbackDto created = feedbackService.createFeedback(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(feedbackService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAll() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<FeedbackDto>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(feedbackService.getByCustomer(customerId));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<FeedbackDto>> getByBooking(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(feedbackService.getByBooking(bookingId));
    }

    @GetMapping("/facility/{facilityBookingId}")
    public ResponseEntity<List<FeedbackDto>> getByFacility(@PathVariable Integer facilityBookingId) {
        return ResponseEntity.ok(feedbackService.getByFacilityBooking(facilityBookingId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDto> update(@PathVariable Integer id,
                                              @RequestBody FeedbackDto dto) {
        return ResponseEntity.ok(feedbackService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        feedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Admin dashboard list ----------------
    // RETURNS: feedbackId, customerName, itemName, rating, comments
    @GetMapping("/admin/list")
    public ResponseEntity<List<FeedbackDisplayDto>> getAdminList() {
        return ResponseEntity.ok(feedbackService.getAdminDisplayList());
    }
}