package com.hotel.management.payment;

import com.hotel.management.payment.PaymentRequest;
import com.hotel.management.bookings.BookingsRepository;
import com.hotel.management.users.UserDTO;
import com.hotel.management.users.UserService;
import com.hotel.management.facilitybooking.FacilityBookingRepository;
import com.hotel.management.notifications.EmailService;
import com.hotel.management.notifications.NotificationService;
import com.hotel.management.notifications.NotificationType;
import com.hotel.management.payment.Payment;
import com.hotel.management.payment.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingsRepository bookingRepo;
    private final FacilityBookingRepository facilityRepo;
    private final NotificationService notificationService;
    private final ReceiptPDFGenerator pdfGenerator;
    private final EmailService emailService;
    private final UserService userService;

    public PaymentService(PaymentRepository paymentRepo,
                          BookingsRepository bookingRepo,
                          FacilityBookingRepository facilityRepo,
                          NotificationService notificationService,
                          ReceiptPDFGenerator pdfGenerator,
                          EmailService emailService,
                          UserService userService) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
        this.facilityRepo = facilityRepo;
        this.notificationService = notificationService;
        this.pdfGenerator = pdfGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }

    /**
     * processPayment:
     * - validate booking/facility ownership (if bookingId/facilityBookingId provided)
     * - prevent obvious duplicate payments (same booking + same amount within short window)
     * - generate receipt PDF, save to disk, persist payment including receipt path
     * - send email and notification
     */
    @Transactional
    public String processPayment(PaymentRequest req) throws Exception {

        // --- Basic validations ---
        if (req.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID required");
        }
        if (req.getAmountPaid() == null || req.getAmountPaid() < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        // Validate booking ownership / existence if bookingId provided
        if (req.getBookingId() != null) {
            // The bookingsRepo must provide a method: Optional<Booking> findById(int id)
            // or boolean existsByIdAndCustomerId(bookingId, customerId)
            boolean bookingOk = bookingRepo.existsByIdAndCustomerId(req.getBookingId(), req.getCustomerId());
            if (!bookingOk) {
                throw new IllegalArgumentException("Booking not found or does not belong to customer.");
            }
        }

        // Validate facility booking similarly (optional)
        if (req.getFacilityBookingId() != null) {
            boolean facilityOk = facilityRepo.existsByIdAndCustomerId(req.getFacilityBookingId(), req.getCustomerId());
            if (!facilityOk) {
                throw new IllegalArgumentException("Facility booking not found or does not belong to customer.");
            }
        }

        // Prevent duplicate: same booking & amount recently processed
        if (req.getBookingId() != null) {
            boolean existsRecent = paymentRepo.existsRecentPaymentForBooking(req.getBookingId(), req.getAmountPaid(), 5); // 5 min window
            if (existsRecent) {
                throw new IllegalStateException("A payment with same amount for this booking was recently processed.");
            }
        }

        // --- Now generate PDF receipt and save file ---
        UserDTO customer = userService.getUserById(req.getCustomerId());
        if (customer == null) throw new IllegalArgumentException("Customer not found");

        // Generate and save receipt to disk (returns bytes + path)
        ReceiptPDFGenerator.ReceiptResult rr = pdfGenerator.generateReceiptAndSave(
                java.util.UUID.randomUUID().toString(), // temp transaction id (we replace by repo-generated tx)
                req.getAmountPaid(),
                req.getBookingId(),
                req.getFacilityBookingId(),
                customer.getName()
        );

        // Create DB record and return persistent payment object (this method will generate real transactionId)
        Payment p = paymentRepo.createPaymentAndReturn(req, rr.path);

        // Build notification parts
        Map<String, Object> data = Map.of(
                "bookingId", p.getBookingId(),
                "amount", p.getAmountPaid(),
                "transactionId", p.getTransactionId()
        );

        Map<String, String> parts = notificationService.buildEmailParts(
                req.getCustomerId(),
                NotificationType.PAYMENT_RECEIVED,
                data
        );

        String subject = parts.get("subject");
        String body = parts.get("body");

        // Send email with PDF bytes
        boolean sent = emailService.sendHtmlEmailWithAttachment(
                customer.getEmail(),
                subject,
                body,
                rr.bytes,
                "payment_receipt.pdf"
        );

        // Save notification record
        notificationService.sendNotification(
                req.getCustomerId(),
                NotificationType.PAYMENT_RECEIVED,
                data
        );

        // Return transaction id (persisted)
        return p.getTransactionId();
    }
}
