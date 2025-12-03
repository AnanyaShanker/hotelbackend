package com.hotel.management.facilitybooking;

import com.hotel.management.facilities.FacilityService;
import com.hotel.management.notifications.NotificationService;
import com.hotel.management.notifications.NotificationType;
import com.hotel.management.payment.Payment;
import com.hotel.management.payment.PaymentRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Component
public class FacilityBookingCreatedListener {

    private final NotificationService notificationService;
    private final FacilityService facilityService;
    private final PaymentRepository paymentRepository;

    public FacilityBookingCreatedListener(NotificationService notificationService,
                                          FacilityService facilityService,
                                          PaymentRepository paymentRepository) {
        this.notificationService = notificationService;
        this.facilityService = facilityService;
        this.paymentRepository = paymentRepository;
    }

    @EventListener
    @Transactional
    public void onFacilityBookingCreated(FacilityBookedEvent event) throws Exception {

        FacilityBooking booking = event.getBooking();

        // 1. Create pending payment record automatically
        Payment payment = new Payment();
        payment.setFacilityBookingId(booking.getFacilityBookingId());
        payment.setCustomerId(booking.getCustomerId());
        payment.setAmountPaid(booking.getTotalPrice());
        payment.setPaymentMethod("CASH"); // Default method, user can update later
        payment.setStatus("PENDING"); // Payment is pending until customer completes it
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setNotes("Auto-created for facility booking #" + booking.getFacilityBookingId());

        // Insert and get back with generated ID
        payment = paymentRepository.createPaymentAndReturnWithId(payment);

        // 2. Fetch facility details for notification
        var facility = facilityService.getFacilityById(booking.getFacilityId());

        // 3. Send notification
        notificationService.sendNotification(
                booking.getCustomerId(),
                NotificationType.FACILITY_BOOKING_CONFIRMATION,
                Map.of(
                        "facilityBookingId", String.valueOf(booking.getFacilityBookingId()),
                        "facilityName", facility.getName(),
                        "facilityType", facility.getType(),
                        "bookingDate", booking.getBookingDate(),
                        "timeSlot", booking.getStartTime() + " - " + booking.getEndTime(),
                        "totalPrice", String.valueOf(booking.getTotalPrice()),
                        "paymentId", String.valueOf(payment.getPaymentId()),
                        "transactionId", payment.getTransactionId()
                )
        );

        // 4. Send payment pending notification
        notificationService.sendNotification(
                booking.getCustomerId(),
                NotificationType.PAYMENT_PENDING,
                Map.of(
                        "paymentId", String.valueOf(payment.getPaymentId()),
                        "facilityBookingId", String.valueOf(booking.getFacilityBookingId()),
                        "amount", String.valueOf(payment.getAmountPaid()),
                        "transactionId", payment.getTransactionId(),
                        "status", "PENDING",
                        "message", "Please complete your payment to confirm your facility booking."
                )
        );
    }
}




