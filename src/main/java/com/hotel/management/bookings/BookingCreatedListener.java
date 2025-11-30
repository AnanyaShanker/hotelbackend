package com.hotel.management.bookings;



import com.hotel.management.notifications.NotificationService;
import com.hotel.management.notifications.NotificationType;
import com.hotel.management.rooms.RoomService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BookingCreatedListener {

    private final NotificationService notificationService;
    private final RoomService roomService;

    public BookingCreatedListener(NotificationService notificationService,
                                  RoomService roomService) {
        this.notificationService = notificationService;
        this.roomService = roomService;
    }

    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) throws Exception {

        Bookings booking = event.getBooking();

        // fetch room details
        var room = roomService.getRoomById(booking.getRoomId());

        notificationService.sendNotification(
                booking.getCustomerId(),
                NotificationType.BOOKING_CONFIRMATION,
                Map.of(
                        "bookingId", String.valueOf(booking.getBookingId()),
                        "roomNumber", room.getRoomNumber(),
                        "roomType", room.getRoomId(),
                        "checkIn", booking.getCheckInDate().toString(),
                        "checkOut", booking.getCheckOutDate().toString(),
                        "totalDays", String.valueOf(booking.getTotalDays()),
                        "totalPrice", String.valueOf(booking.getTotalPrice())
                )
        );
    }
}
