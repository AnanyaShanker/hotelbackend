package com.hotel.management.facilitybooking;

public class FacilityBookedEvent {

    private final FacilityBooking booking;

    public FacilityBookedEvent(FacilityBooking booking) {
        this.booking = booking;
    }

    public FacilityBooking getBooking() {
        return booking;
    }
}
