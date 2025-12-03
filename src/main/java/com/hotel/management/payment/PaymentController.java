package com.hotel.management.payment;

import com.hotel.management.notifications.EmailService;
import com.hotel.management.notifications.NotificationService;
import com.hotel.management.notifications.NotificationType;
import com.hotel.management.users.UserDTO;
import com.hotel.management.users.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;
    private final PaymentRepository repository;
    private final NotificationService notificationService;
    private final ReceiptPDFGenerator pdfGenerator;
    private final EmailService emailService;
    private final UserService userService;

    public PaymentController(PaymentService service,
                            PaymentRepository repository,
                            NotificationService notificationService,
                            ReceiptPDFGenerator pdfGenerator,
                            EmailService emailService,
                            UserService userService) {
        this.service = service;
        this.repository = repository;
        this.notificationService = notificationService;
        this.pdfGenerator = pdfGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/process")
   // @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest req) throws Exception {
        String tx = service.processPayment(req);
        return ResponseEntity.ok("Payment Successful. Transaction ID: " + tx);
    }

    // Get payments for current/any customer (admin can fetch others)
    @GetMapping("/customer/{customerId}")
    //@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Payment>> getPaymentsForCustomer(@PathVariable int customerId) {
        List<Payment> list = repository.findPaymentsByCustomer(customerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable Integer bookingId) {
        Payment payment = repository.findByBookingId(bookingId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/facility-booking/{facilityBookingId}")
    public ResponseEntity<Payment> getPaymentByFacilityBookingId(@PathVariable Integer facilityBookingId) {
        Payment payment = repository.findByFacilityBookingId(facilityBookingId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer paymentId) {
        Payment payment = repository.findById(paymentId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam String status) throws Exception {
        Payment existing = repository.findById(paymentId);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // Update payment status
        repository.updatePaymentStatus(paymentId, status);

        // Send notification if status changed to SUCCESS
        if ("SUCCESS".equalsIgnoreCase(status)) {
            Map<String, Object> notificationData = Map.of(
                    "paymentId", paymentId,
                    "bookingId", existing.getBookingId() != null ? existing.getBookingId() : 0,
                    "facilityBookingId", existing.getFacilityBookingId() != null ? existing.getFacilityBookingId() : 0,
                    "amount", existing.getAmountPaid(),
                    "transactionId", existing.getTransactionId(),
                    "paymentMethod", existing.getPaymentMethod()
            );

            notificationService.sendNotification(
                    existing.getCustomerId(),
                    NotificationType.PAYMENT_RECEIVED,
                    notificationData
            );
        }

        return ResponseEntity.ok("Payment status updated to: " + status);
    }

    @PostMapping("/pay-now")
    public ResponseEntity<?> processPaymentNow(@RequestBody PaymentRequest request) throws Exception {
        // Simulate instant payment processing (like Stripe, PayPal, Razorpay)
        // In real implementation, this would call actual payment gateway

        // Random success/failure simulation (80% success rate)
        boolean paymentSucceeded = Math.random() < 0.8;

        String status = paymentSucceeded ? "SUCCESS" : "FAILED";
        String transactionId = "TXN-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Create payment record with immediate status
        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setFacilityBookingId(request.getFacilityBookingId());
        payment.setCustomerId(request.getCustomerId());
        payment.setAmountPaid(request.getAmountPaid());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(status);
        payment.setTransactionId(transactionId);
        payment.setNotes(paymentSucceeded ? "Payment processed successfully" : "Payment failed - please try again");

        repository.createPayment(payment);

        // Send notification (success or failure)
        NotificationType notificationType = paymentSucceeded
                ? NotificationType.PAYMENT_RECEIVED
                : NotificationType.SYSTEM_ALERT;

        Map<String, Object> notificationData = Map.of(
                "transactionId", transactionId,
                "bookingId", request.getBookingId() != null ? request.getBookingId() : 0,
                "facilityBookingId", request.getFacilityBookingId() != null ? request.getFacilityBookingId() : 0,
                "amount", request.getAmountPaid(),
                "paymentMethod", request.getPaymentMethod(),
                "status", status,
                "message", paymentSucceeded
                        ? "Your payment was successful!"
                        : "Payment failed. Please try again or use a different payment method."
        );

        notificationService.sendNotification(
                request.getCustomerId(),
                notificationType,
                notificationData
        );

        // ✅ Generate PDF receipt and send email with attachment (only on success)
        if (paymentSucceeded) {
            try {
                // Get customer details
                UserDTO customer = userService.getUserById(request.getCustomerId());

                // Generate PDF receipt
                ReceiptPDFGenerator.ReceiptResult receipt = pdfGenerator.generateReceiptAndSave(
                        transactionId,
                        request.getAmountPaid(),
                        request.getBookingId(),
                        request.getFacilityBookingId(),
                        customer.getName()
                );

                // Update payment record with receipt path
                Payment createdPayment = repository.findByTransactionId(transactionId);
                if (createdPayment != null) {
                    repository.updatePaymentReceipt(createdPayment.getPaymentId(), receipt.path);
                }

                // Send email with PDF attachment
                String emailHtml = buildPaymentReceiptEmail(customer.getName(), transactionId, request.getAmountPaid());
                emailService.sendHtmlEmailWithAttachment(
                        customer.getEmail(),
                        "Payment Receipt - Transaction #" + transactionId,
                        emailHtml,
                        receipt.bytes,
                        "receipt_" + transactionId + ".pdf"
                );

            } catch (Exception pdfEx) {
                // Don't fail the payment if PDF generation fails
                pdfEx.printStackTrace();
            }
        }

        // Return response
        if (paymentSucceeded) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment successful! Receipt sent to your email.",
                    "transactionId", transactionId,
                    "status", "SUCCESS"
            ));
        } else {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", "Payment failed. Please try again.",
                    "transactionId", transactionId,
                    "status", "FAILED"
            ));
        }
    }

    private String buildPaymentReceiptEmail(String customerName, String transactionId, Double amount) {
        return "<html><body>" +
                "<h2>Payment Confirmation</h2>" +
                "<p>Dear " + customerName + ",</p>" +
                "<p>Your payment has been successfully processed!</p>" +
                "<ul>" +
                "<li><strong>Transaction ID:</strong> " + transactionId + "</li>" +
                "<li><strong>Amount Paid:</strong> Rs " + amount + "</li>" +
                "<li><strong>Date:</strong> " + java.time.LocalDateTime.now() + "</li>" +
                "</ul>" +
                "<p>Please find your receipt attached to this email.</p>" +
                "<p>Thank you for your payment!</p>" +
                "<p>Best regards,<br/>Hotel Management Team</p>" +
                "</body></html>";
    }
}
