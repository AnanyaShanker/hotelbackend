package com.hotel.management.bookings;



import com.hotel.management.notifications.NotificationService;
import com.hotel.management.notifications.NotificationType;
import com.hotel.management.rooms.RoomService;
import com.hotel.management.payment.Payment;
import com.hotel.management.payment.PaymentRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Component
public class BookingCreatedListener {

    private final NotificationService notificationService;
    private final RoomService roomService;
    private final PaymentRepository paymentRepository;

    public BookingCreatedListener(NotificationService notificationService,
                                  RoomService roomService,
                                  PaymentRepository paymentRepository) {
        this.notificationService = notificationService;
        this.roomService = roomService;
        this.paymentRepository = paymentRepository;
    }

    @EventListener
    @Transactional
    public void onBookingCreated(BookingCreatedEvent event) throws Exception {

        Bookings booking = event.getBooking();

       
        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setCustomerId(booking.getCustomerId());
        payment.setAmountPaid(booking.getTotalPrice());
        payment.setPaymentMethod("CASH"); // Default method, user can update later
        payment.setStatus("PENDING"); // Payment is pending until customer completes it
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setNotes("Auto-created for room booking #" + booking.getBookingId());

        
        payment = paymentRepository.createPaymentAndReturnWithId(payment);

        
        var room = roomService.getRoomById(booking.getRoomId());

        
        notificationService.sendNotification(
                booking.getCustomerId(),
                NotificationType.BOOKING_CONFIRMATION,
                Map.of(
                        "bookingId", String.valueOf(booking.getBookingId()),
                        "roomNumber", room.getRoomNumber(),
                        "roomType", String.valueOf(room.getTypeId()),
                        "checkIn", booking.getCheckInDate().toString(),
                        "checkOut", booking.getCheckOutDate().toString(),
                        "totalPrice", String.valueOf(booking.getTotalPrice())
                )
        );

        
        notificationService.sendNotification(
                booking.getCustomerId(),
                NotificationType.PAYMENT_PENDING,
                Map.of(
                        "paymentId", String.valueOf(payment.getPaymentId()),
                        "bookingId", String.valueOf(booking.getBookingId()),
                        "amount", String.valueOf(payment.getAmountPaid()),
                        "transactionId", payment.getTransactionId(),
                        "status", "PENDING",
                        "message", "Please complete your payment to confirm your booking."
                )
        );
    }
}
