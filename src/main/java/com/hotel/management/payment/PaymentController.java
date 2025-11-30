package com.hotel.management.payment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;
    private final PaymentRepository repository;

    public PaymentController(PaymentService service, PaymentRepository repository) {
        this.service = service;
        this.repository = repository;
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
}
