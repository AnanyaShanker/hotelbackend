package com.hotel.management.facilitybooking;


import org.springframework.context.ApplicationEventPublisher;
import com.hotel.management.facilities.Facility;
import com.hotel.management.facilities.FacilityRepositoryImpl;
import com.hotel.management.users.UserRepository;
import com.hotel.management.notifications.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FacilityBookingService {

    private final FacilityBookingRepository bookingRepository;
    private final FacilityRepositoryImpl facilityRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ApplicationEventPublisher applicationEventPublisher; // ⬅️ ADDED

    public FacilityBookingService(FacilityBookingRepository bookingRepository,
                                  FacilityRepositoryImpl facilityRepository,
                                  UserRepository userRepository,
                                  EmailService emailService,
                                  ApplicationEventPublisher applicationEventPublisher) { // ⬅️ ADDED
        this.bookingRepository = bookingRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.applicationEventPublisher = applicationEventPublisher; // ⬅️ ADDED
    }

    /**
     * Create a booking with DB row locking (FOR UPDATE).
     */
    @Transactional(rollbackFor = Exception.class)
    public FacilityBooking createBooking(FacilityBookingRequest req) {
        // Basic validation
        if (req.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be >= 1");

        if (req.getBookingDate() == null)
            throw new IllegalArgumentException("bookingDate is required (yyyy-MM-dd)");

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(req.getBookingDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid bookingDate format; expected yyyy-MM-dd");
        }

        LocalTime start = null, end = null;
        if (req.getStartTime() != null || req.getEndTime() != null) {
            if (req.getStartTime() == null || req.getEndTime() == null) {
                throw new IllegalArgumentException("Both startTime and endTime must be provided for time-based booking");
            }
            try {
                start = LocalTime.parse(req.getStartTime());
                end = LocalTime.parse(req.getEndTime());
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid time format; expected HH:mm");
            }
            if (!end.isAfter(start)) {
                throw new IllegalArgumentException("endTime must be after startTime");
            }
        }

        // 1) Verify user exists & active
        if (!userRepository.existsByIdAndActive(req.getCustomerId())) {
            throw new IllegalArgumentException("Customer not found or not active: " + req.getCustomerId());
        }

        // 2) Verify facility exists and available
        Facility facility = facilityRepository.findById(req.getFacilityId());
        if (facility == null)
            throw new IllegalArgumentException("Facility not found: " + req.getFacilityId());

        if (facility.getStatus() != null &&
                !"AVAILABLE".equalsIgnoreCase(facility.getStatus()) &&
                !"active".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalStateException("Facility is not available for booking");
        }

        // 3) Lock existing rows for this facility+date (FOR UPDATE)
        List<FacilityBooking> existingLocked =
                bookingRepository.findByFacilityAndDateForUpdate(facility.getFacilityId(), bookingDate);

        // 4) Check availability
        if (!isAvailable(existingLocked, start, end, req.getQuantity(), facility.getCapacity())) {
            throw new IllegalStateException("Facility not available for requested date/time or capacity exceeded");
        }

        // 5) Price calculation
        double totalPrice = calculatePrice(facility.getPrice(), start, end, req.getQuantity());

        // 6) Build booking object
        FacilityBooking booking = new FacilityBooking();
        booking.setCustomerId(req.getCustomerId());
        booking.setFacilityId(req.getFacilityId());
        booking.setBookingDate(bookingDate.toString());
        booking.setStartTime(start != null ? start.toString() : null);
        booking.setEndTime(end != null ? end.toString() : null);
        booking.setTimeSlot(req.getTimeSlot());
        booking.setQuantity(req.getQuantity());
        booking.setTotalPrice(round(totalPrice));
        booking.setBookingStatus("CONFIRMED");
        booking.setNotes(req.getNotes());

        // 7) Save and get ID
        int newId = bookingRepository.saveAndReturnId(booking);

        // 8) Fetch complete booking
        FacilityBooking saved = bookingRepository.findById(newId);
        
        applicationEventPublisher.publishEvent(new FacilityBookedEvent(saved));

        // 9) Send booking confirmation email ⬅️ ADDED
//        try {
//            String customerEmail = userRepository.getEmailById(saved.getCustomerId());
//            String html = facilityBookingTemplate(saved);
//            emailService.sendHtmlEmail(customerEmail, "Facility Booking Confirmation", html);
//        } catch (Exception emailEx) {
//            // DO NOT rollback booking if email fails
//            emailEx.printStackTrace();
//        }

        return saved;
    }

    // ---------------------------
    // Availability logic
    // ---------------------------
    private boolean isAvailable(List<FacilityBooking> existing,
                                LocalTime start,
                                LocalTime end,
                                int requestedQuantity,
                                int facilityCapacity) {
        if (existing == null || existing.isEmpty()) {
            return requestedQuantity <= facilityCapacity;
        }

        if (start == null || end == null) {
            int sum = existing.stream().mapToInt(FacilityBooking::getQuantity).sum();
            return (sum + requestedQuantity) <= facilityCapacity;
        } else {
            int overlapSum = 0;
            for (FacilityBooking b : existing) {
                if (b.getStartTime() == null || b.getEndTime() == null) {
                    overlapSum += b.getQuantity();
                    continue;
                }
                LocalTime s = LocalTime.parse(b.getStartTime());
                LocalTime e = LocalTime.parse(b.getEndTime());
                boolean overlap = start.isBefore(e) && end.isAfter(s);
                if (overlap) overlapSum += b.getQuantity();
            }
            return (overlapSum + requestedQuantity) <= facilityCapacity;
        }
    }

    public FacilityBooking getBookingById(int id) {
        return bookingRepository.findById(id);
    }

    public FacilityBookingFullDTO getFullBookingById(int id) {
        return bookingRepository.findFullById(id);
    }

    public List<FacilityBooking> getBookingsForFacility(int facilityId, LocalDate date) {
        return bookingRepository.findByFacilityAndDate(facilityId, date);
    }

    public List<FacilityBooking> getBookingsByCustomer(int customerId) {
        return bookingRepository.findByCustomer(customerId);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBooking(int bookingId, int requestedByUserId) {
        return bookingRepository.updateStatus(bookingId, "CANCELLED");
    }

    public double calculatePrice(double facilityPrice, LocalTime start, LocalTime end, int quantity) {
        if (start != null && end != null) {
            long seconds = ChronoUnit.SECONDS.between(start, end);
            double hours = seconds / 3600.0;
            return facilityPrice * hours * quantity;
        } else {
            return facilityPrice * quantity;
        }
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // ---------------------------
    // Email Template (simple demo)
    // ---------------------------
    private String facilityBookingTemplate(FacilityBooking b) {
        return String.format("""
                <h2>Your Facility Booking is Confirmed</h2>
                <p>Thank you for booking with us!</p>
                <p><b>Facility ID:</b> %d</p>
                <p><b>Date:</b> %s</p>
                <p><b>Time:</b> %s</p>
                <p><b>Quantity:</b> %d</p>
                <p><b>Total Price:</b> ₹%.2f</p>
                <br>
                <p>We look forward to serving you.</p>
                """,
                b.getFacilityId(),
                b.getBookingDate(),
                (b.getStartTime() != null ? b.getStartTime() + " - " + b.getEndTime() : "Full Day"),
                b.getQuantity(),
                b.getTotalPrice()
        );
    }
}



