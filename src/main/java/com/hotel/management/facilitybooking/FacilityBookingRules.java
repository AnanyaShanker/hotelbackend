package com.hotel.management.facilitybooking;



import java.time.*;
import java.time.temporal.ChronoUnit;


public class FacilityBookingRules {

    
    public static final LocalTime FACILITY_OPEN_TIME = LocalTime.of(6, 0); 
    public static final LocalTime FACILITY_CLOSE_TIME = LocalTime.of(23, 0);

    
    public static final int MIN_BOOKING_MINUTES = 60;  // 1 hour minimum
    public static final int MAX_BOOKING_HOURS = 12;    // 12 hours maximum

   
    public static final int MIN_ADVANCE_HOURS = 2;     // 2 hours notice
    public static final int MAX_ADVANCE_DAYS = 90;     // 90 days ahead

    
    public static final int BUFFER_MINUTES_SPA = 30;
    public static final int BUFFER_MINUTES_GYM = 15;
    public static final int BUFFER_MINUTES_POOL = 30;
    public static final int BUFFER_MINUTES_BANQUET = 60;
    public static final int BUFFER_MINUTES_MEETING_HALL = 15;
    public static final int BUFFER_MINUTES_RESTAURANT = 30;
    public static final int BUFFER_MINUTES_OTHER = 15;

    
    public static final int MAX_ACTIVE_BOOKINGS_PER_CUSTOMER = 3;
    public static final int MAX_BOOKINGS_PER_DAY_PER_CUSTOMER = 2;


    /**
     * Validate booking time is within operating hours
     */
    public static void validateOperatingHours(LocalTime start, LocalTime end) {
        if (start.isBefore(FACILITY_OPEN_TIME)) {
            throw new IllegalArgumentException(
                String.format("Booking cannot start before %s. Facilities open at %s.",
                    FACILITY_OPEN_TIME, FACILITY_OPEN_TIME)
            );
        }
        if (end.isAfter(FACILITY_CLOSE_TIME)) {
            throw new IllegalArgumentException(
                String.format("Booking cannot end after %s. Facilities close at %s.",
                    FACILITY_CLOSE_TIME, FACILITY_CLOSE_TIME)
            );
        }
    }

    /**
     * Validate booking duration (min and max limits)
     */
    public static void validateDuration(LocalTime start, LocalTime end) {
        long minutes = ChronoUnit.MINUTES.between(start, end);

        if (minutes < MIN_BOOKING_MINUTES) {
            throw new IllegalArgumentException(
                String.format("Minimum booking duration is %d minutes. Your booking is %d minutes.",
                    MIN_BOOKING_MINUTES, minutes)
            );
        }

        long maxMinutes = MAX_BOOKING_HOURS * 60L;
        if (minutes > maxMinutes) {
            throw new IllegalArgumentException(
                String.format("Maximum booking duration is %d hours. Your booking is %.1f hours.",
                    MAX_BOOKING_HOURS, minutes / 60.0)
            );
        }
    }

    /**
     * Validate advance booking window (not too soon, not too far)
     */
    public static void validateAdvanceBooking(LocalDate bookingDate, LocalTime bookingStartTime) {
        LocalDateTime bookingDateTime = LocalDateTime.of(
            bookingDate,
            bookingStartTime != null ? bookingStartTime : LocalTime.of(9, 0)
        );
        LocalDateTime now = LocalDateTime.now();

        // Check if booking is in the past
        if (bookingDateTime.isBefore(now)) {
            throw new IllegalArgumentException("Cannot book in the past");
        }

        // Check minimum advance time
        long hoursUntilBooking = ChronoUnit.HOURS.between(now, bookingDateTime);
        if (hoursUntilBooking < MIN_ADVANCE_HOURS) {
            throw new IllegalArgumentException(
                String.format("Bookings must be made at least %d hours in advance. You are trying to book in %d hours.",
                    MIN_ADVANCE_HOURS, hoursUntilBooking)
            );
        }

        // Check maximum advance time
        long daysUntilBooking = ChronoUnit.DAYS.between(now.toLocalDate(), bookingDate);
        if (daysUntilBooking > MAX_ADVANCE_DAYS) {
            throw new IllegalArgumentException(
                String.format("Bookings can only be made up to %d days in advance. You are trying to book %d days ahead.",
                    MAX_ADVANCE_DAYS, daysUntilBooking)
            );
        }
    }

    /**
     * Get buffer time (cleanup time) based on facility type
     */
    public static int getBufferMinutes(String facilityType) {
        if (facilityType == null || facilityType.trim().isEmpty()) {
            return BUFFER_MINUTES_OTHER;
        }

        return switch (facilityType.toUpperCase()) {
            case "SPA" -> BUFFER_MINUTES_SPA;
            case "GYM" -> BUFFER_MINUTES_GYM;
            case "POOL" -> BUFFER_MINUTES_POOL;
            case "BANQUET" -> BUFFER_MINUTES_BANQUET;
            case "MEETING_HALL" -> BUFFER_MINUTES_MEETING_HALL;
            case "RESTAURANT" -> BUFFER_MINUTES_RESTAURANT;
            default -> BUFFER_MINUTES_OTHER;
        };
    }



    /**
     * Validate facility-specific booking slot requirements
     * Example: SPA allows only 60, 90, or 120 minute sessions
     */
    public static void validateAllowedSlots(String facilityType, LocalTime start, LocalTime end) {
        if (facilityType == null) return;

        long minutes = ChronoUnit.MINUTES.between(start, end);

        if ("SPA".equalsIgnoreCase(facilityType)) {
            int[] allowedDurations = {60, 90, 120}; // 1h, 1.5h, 2h

            boolean isValid = false;
            for (int allowed : allowedDurations) {
                if (minutes == allowed) {
                    isValid = true;
                    break;
                }
            }

            if (!isValid) {
                throw new IllegalArgumentException(
                    String.format("SPA bookings must be 60, 90, or 120 minutes. Your booking is %d minutes.", minutes)
                );
            }
        }

        // Add other facility-specific validations as needed
        // Example: Meeting halls in 2-hour blocks
        // Example: Pool sessions in 1-hour or 2-hour slots
    }



    /**
     * Validate customer hasn't exceeded booking limits
     */
    public static void validateCustomerBookingLimits(int activeBookingsCount) {
        if (activeBookingsCount >= MAX_ACTIVE_BOOKINGS_PER_CUSTOMER) {
            throw new IllegalStateException(
                String.format("You have reached the maximum of %d active bookings. Please cancel or complete existing bookings before creating new ones.",
                    MAX_ACTIVE_BOOKINGS_PER_CUSTOMER)
            );
        }
    }
}

