package com.hotel.management.payment;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PaymentRowMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));

        
        Integer bookingId = (Integer) rs.getObject("booking_id");
        Integer facilityBookingId = (Integer) rs.getObject("facility_booking_id");
        Integer customerId = (Integer) rs.getObject("customer_id");

        p.setBookingId(bookingId);
        p.setFacilityBookingId(facilityBookingId);
        p.setCustomerId(customerId);

        p.setPaymentMethod(rs.getString("payment_method"));
        p.setAmountPaid(rs.getDouble("amount_paid"));

        
        Timestamp ts = rs.getTimestamp("payment_date");
        p.setPaymentDate(ts != null ? ts.toString() : null);

        p.setTransactionId(rs.getString("transaction_id"));
        p.setStatus(rs.getString("status"));
        p.setPaymentReceipt(rs.getString("payment_receipt"));
        p.setNotes(rs.getString("notes"));
        return p;
    }
}
