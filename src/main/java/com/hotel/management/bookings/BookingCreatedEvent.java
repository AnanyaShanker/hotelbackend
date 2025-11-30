package com.hotel.management.bookings;

public class BookingCreatedEvent {
    private final Bookings booking;

    public BookingCreatedEvent(Bookings booking) {
        this.booking = booking;
    }

    public Bookings getBooking() {
        return booking;
    }
}
