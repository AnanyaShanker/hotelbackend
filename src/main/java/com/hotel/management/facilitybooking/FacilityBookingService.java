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
    private final ApplicationEventPublisher applicationEventPublisher;

    public FacilityBookingService(FacilityBookingRepository bookingRepository,
                                  FacilityRepositoryImpl facilityRepository,
                                  UserRepository userRepository,
                                  EmailService emailService,
                                  ApplicationEventPublisher applicationEventPublisher) { 
        this.bookingRepository = bookingRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.applicationEventPublisher = applicationEventPublisher; 
    }

   
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

            
            FacilityBookingRules.validateOperatingHours(start, end);

            
            FacilityBookingRules.validateDuration(start, end);

            
            FacilityBookingRules.validateAdvanceBooking(bookingDate, start);
        } else {
           
            FacilityBookingRules.validateAdvanceBooking(bookingDate, LocalTime.of(9, 0));
        }

        
        if (!userRepository.existsByIdAndActive(req.getCustomerId())) {
            throw new IllegalArgumentException("Customer not found or not active: " + req.getCustomerId());
        }

        
        Facility facility = facilityRepository.findById(req.getFacilityId());
        if (facility == null)
            throw new IllegalArgumentException("Facility not found: " + req.getFacilityId());

        if (facility.getStatus() != null &&
                !"AVAILABLE".equalsIgnoreCase(facility.getStatus()) &&
                !"active".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalStateException("Facility is not available for booking");
        }

       
        if (start != null && end != null) {
            FacilityBookingRules.validateAllowedSlots(facility.getType(), start, end);
        }

        
        int activeBookings = bookingRepository.countActiveBookingsByCustomer(req.getCustomerId());
        FacilityBookingRules.validateCustomerBookingLimits(activeBookings);

        
        List<FacilityBooking> existingLocked =
                bookingRepository.findByFacilityAndDateForUpdate(facility.getFacilityId(), bookingDate);

        
        int bufferMinutes = FacilityBookingRules.getBufferMinutes(facility.getType());
        if (!isAvailableWithBuffer(existingLocked, start, end, req.getQuantity(),
                                    facility.getCapacity(), bufferMinutes)) {
            throw new IllegalStateException(
                String.format("Facility not available for requested date/time (includes %d min buffer time for cleaning)",
                    bufferMinutes)
            );
        }

        
        double totalPrice = calculatePrice(facility.getPrice(), start, end, req.getQuantity());

        
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

        
        int newId = bookingRepository.saveAndReturnId(booking);

       
        FacilityBooking saved = bookingRepository.findById(newId);
        
        applicationEventPublisher.publishEvent(new FacilityBookedEvent(saved));

  

        return saved;
    }

    
    /**
     * Check availability considering buffer time between bookings for cleaning
     * Skips cancelled bookings from capacity calculation
     *
     * @param existing List of existing bookings for the facility/date
     * @param start Requested start time (null for full-day)
     * @param end Requested end time (null for full-day)
     * @param requestedQuantity Number of guests
     * @param facilityCapacity Maximum capacity of facility
     * @param bufferMinutes Cleanup time between bookings
     * @return true if available, false otherwise
     */
    private boolean isAvailableWithBuffer(List<FacilityBooking> existing,
                                          LocalTime start,
                                          LocalTime end,
                                          int requestedQuantity,
                                          int facilityCapacity,
                                          int bufferMinutes) {
        if (existing == null || existing.isEmpty()) {
            return requestedQuantity <= facilityCapacity;
        }

        if (start == null || end == null) {
            
            int sum = existing.stream()
                .filter(b -> !"CANCELLED".equals(b.getBookingStatus()))
                .mapToInt(FacilityBooking::getQuantity)
                .sum();
            return (sum + requestedQuantity) <= facilityCapacity;
        } else {
            
            int overlapSum = 0;

            for (FacilityBooking b : existing) {
                
                if ("CANCELLED".equals(b.getBookingStatus())) {
                    continue;
                }

                if (b.getStartTime() == null || b.getEndTime() == null) {
                    
                    overlapSum += b.getQuantity();
                    continue;
                }

                LocalTime existingStart = LocalTime.parse(b.getStartTime());
                LocalTime existingEnd = LocalTime.parse(b.getEndTime());

                
                LocalTime bufferedStart = existingStart.minusMinutes(bufferMinutes);
                LocalTime bufferedEnd = existingEnd.plusMinutes(bufferMinutes);

                
                boolean overlap = start.isBefore(bufferedEnd) && end.isAfter(bufferedStart);

                if (overlap) {
                    overlapSum += b.getQuantity();
                }
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
        FacilityBooking booking = bookingRepository.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
        }

        if (booking.getCustomerId() != requestedByUserId) {
            throw new IllegalArgumentException("You can only cancel your own bookings");
        }

        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        if ("COMPLETED".equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }

       
        return bookingRepository.updateStatus(bookingId, "CANCELLED");
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        bookingRepository.updatePaymentStatus(bookingId, paymentStatus);
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



