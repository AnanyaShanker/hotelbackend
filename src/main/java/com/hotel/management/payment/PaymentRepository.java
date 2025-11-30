package com.hotel.management.payment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbc;

    public PaymentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int createPayment(Payment p) {
        String sql = "INSERT INTO payments (booking_id, facility_booking_id, customer_id, payment_method, amount_paid, transaction_id, status, payment_receipt, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbc.update(sql,
                p.getBookingId(),
                p.getFacilityBookingId(),
                p.getCustomerId(),
                p.getPaymentMethod(),
                p.getAmountPaid(),
                p.getTransactionId(),
                p.getStatus(),
                p.getPaymentReceipt(),
                p.getNotes()
        );
    }

    // returns Payment with generated id and path populated
    public Payment createPaymentAndReturn(PaymentRequest req, String paymentReceiptPath) {
        String sql = "INSERT INTO payments " +
                "(booking_id, facility_booking_id, customer_id, payment_method, amount_paid, transaction_id, status, payment_receipt, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String transactionId = UUID.randomUUID().toString();

        KeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, req.getBookingId());           // nullable
            ps.setObject(2, req.getFacilityBookingId());   // nullable
            ps.setInt(3, req.getCustomerId());
            ps.setString(4, req.getPaymentMethod());
            ps.setDouble(5, req.getAmountPaid());
            ps.setString(6, transactionId);
            ps.setString(7, "SUCCESS");
            ps.setString(8, paymentReceiptPath);
            ps.setString(9, req.getNotes());
            return ps;
        }, kh);

        int paymentId = kh.getKey().intValue();

        
        Payment p = new Payment();
        p.setPaymentId(paymentId);
        p.setBookingId(req.getBookingId());
        p.setFacilityBookingId(req.getFacilityBookingId());
        p.setCustomerId(req.getCustomerId());
        p.setPaymentMethod(req.getPaymentMethod());
        p.setAmountPaid(req.getAmountPaid());
        p.setTransactionId(transactionId);
        p.setStatus("SUCCESS");
        p.setNotes(req.getNotes());
        p.setPaymentReceipt(paymentReceiptPath);

        return p;
    }

    public boolean existsRecentPaymentForBooking(Integer bookingId, Double amount, int minutesWindow) {
        if (bookingId == null) return false;
        String sql = "SELECT COUNT(1) FROM payments WHERE booking_id = ? AND amount_paid = ? AND status = 'SUCCESS' AND payment_date >= ?";
        Timestamp since = Timestamp.from(Instant.now().minusSeconds(minutesWindow * 60L));
        Integer count = jdbc.queryForObject(sql, Integer.class, bookingId, amount, since);
        return count != null && count > 0;
    }

    public List<Payment> findPaymentsByCustomer(int customerId) {
        return jdbc.query("SELECT * FROM payments WHERE customer_id = ? ORDER BY payment_id DESC",
                new PaymentRowMapper(), customerId);
    }

    public List<Payment> findAll() {
        return jdbc.query("SELECT * FROM payments ORDER BY payment_id DESC",
                new PaymentRowMapper());
    }
}
