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

    public Payment createPaymentAndReturnWithId(Payment p) {
        String sql = "INSERT INTO payments (booking_id, facility_booking_id, customer_id, payment_method, amount_paid, transaction_id, status, payment_receipt, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder kh = new GeneratedKeyHolder();

        int rowsAffected = jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, p.getBookingId());
            ps.setObject(2, p.getFacilityBookingId());
            ps.setInt(3, p.getCustomerId());
            ps.setString(4, p.getPaymentMethod());
            ps.setDouble(5, p.getAmountPaid());
            ps.setString(6, p.getTransactionId());
            ps.setString(7, p.getStatus());

            // Handle null payment_receipt
            if (p.getPaymentReceipt() != null) {
                ps.setString(8, p.getPaymentReceipt());
            } else {
                ps.setNull(8, java.sql.Types.VARCHAR);
            }

            // Handle null notes
            if (p.getNotes() != null) {
                ps.setString(9, p.getNotes());
            } else {
                ps.setNull(9, java.sql.Types.VARCHAR);
            }

            return ps;
        }, kh);

        if (kh.getKey() == null) {
            throw new RuntimeException("Failed to retrieve generated payment ID. Rows affected: " + rowsAffected);
        }

        int paymentId = kh.getKey().intValue();
        p.setPaymentId(paymentId);

        return p;
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

        if (kh.getKey() == null) {
            throw new RuntimeException("Failed to retrieve generated payment ID");
        }

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

    public Payment findByBookingId(Integer bookingId) {
        if (bookingId == null) return null;
        String sql = "SELECT * FROM payments WHERE booking_id = ? ORDER BY payment_id DESC LIMIT 1";
        List<Payment> results = jdbc.query(sql, new PaymentRowMapper(), bookingId);
        return results.isEmpty() ? null : results.get(0);
    }

    public Payment findByFacilityBookingId(Integer facilityBookingId) {
        if (facilityBookingId == null) return null;
        String sql = "SELECT * FROM payments WHERE facility_booking_id = ? ORDER BY payment_id DESC LIMIT 1";
        List<Payment> results = jdbc.query(sql, new PaymentRowMapper(), facilityBookingId);
        return results.isEmpty() ? null : results.get(0);
    }

    public Payment findById(Integer paymentId) {
        if (paymentId == null) return null;
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        List<Payment> results = jdbc.query(sql, new PaymentRowMapper(), paymentId);
        return results.isEmpty() ? null : results.get(0);
    }

    public int updatePaymentStatus(Integer paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        return jdbc.update(sql, status, paymentId);
    }

    public int updatePaymentMethodAndStatus(Integer paymentId, String method, String status) {
        String sql = "UPDATE payments SET payment_method = ?, status = ? WHERE payment_id = ?";
        return jdbc.update(sql, method, status, paymentId);
    }

    public Payment findByTransactionId(String transactionId) {
        if (transactionId == null) return null;
        String sql = "SELECT * FROM payments WHERE transaction_id = ?";
        List<Payment> results = jdbc.query(sql, new PaymentRowMapper(), transactionId);
        return results.isEmpty() ? null : results.get(0);
    }

    public int updatePaymentReceipt(Integer paymentId, String receiptPath) {
        String sql = "UPDATE payments SET payment_receipt = ? WHERE payment_id = ?";
        return jdbc.update(sql, receiptPath, paymentId);
    }
}







