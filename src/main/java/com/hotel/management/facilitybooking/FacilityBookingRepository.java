package com.hotel.management.facilitybooking;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FacilityBookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public FacilityBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveAndReturnId(FacilityBooking b) {
        String sql = "INSERT INTO facility_bookings " +
                "(customer_id, facility_id, booking_date, start_time, end_time, time_slot, quantity, total_price, payment_status, booking_status, notes, created_at, updated_at) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql,
                b.getCustomerId(),
                b.getFacilityId(),
                b.getBookingDate(),
                b.getStartTime(),
                b.getEndTime(),
                b.getTimeSlot(),
                b.getQuantity(),
                b.getTotalPrice(),
                b.getPaymentStatus(),
                b.getBookingStatus(),
                b.getNotes()
        );
        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        return id == null ? -1 : id;
    }

    public FacilityBooking findById(int id) {
        String sql = "SELECT * FROM facility_bookings WHERE facility_booking_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rn) -> map(rs), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.err.println("❌ No facility booking found with ID: " + id);
            return null;
        }
    }

    public FacilityBookingFullDTO findFullById(int id) {
        String sql = "SELECT fb.*, u.name AS customer_name, u.email AS customer_email, u.phone AS customer_phone, " +
                "f.name AS facility_name, f.type AS facility_type, f.location AS facility_location, f.capacity AS facility_capacity, " +
                "p.payment_id AS payment_id, p.amount_paid AS payment_amount, p.status AS payment_status_detail " +
                "FROM facility_bookings fb " +
                "LEFT JOIN users u ON u.user_id = fb.customer_id " +
                "LEFT JOIN facilities f ON f.facility_id = fb.facility_id " +
                "LEFT JOIN payments p ON p.facility_booking_id = fb.facility_booking_id " +
                "AND p.payment_id = (SELECT MAX(payment_id) FROM payments WHERE facility_booking_id = fb.facility_booking_id) " +
                "WHERE fb.facility_booking_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rn) -> mapFull(rs), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.err.println("No facility booking found with ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Error fetching facility booking details for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<FacilityBooking> findByFacilityAndDate(int facilityId, LocalDate bookingDate) {
        String sql = "SELECT * FROM facility_bookings WHERE facility_id = ? AND booking_date = ?";
        return jdbcTemplate.query(sql, (rs, rn) -> map(rs), facilityId, bookingDate);
    }

    /**
     * Locks existing bookings rows for this facility+date to avoid races.
     * Must be called inside a @Transactional method.
     */
    public List<FacilityBooking> findByFacilityAndDateForUpdate(int facilityId, LocalDate bookingDate) {
        String sql = "SELECT * FROM facility_bookings WHERE facility_id = ? AND booking_date = ? FOR UPDATE";
        return jdbcTemplate.query(sql, (rs, rn) -> map(rs), facilityId, bookingDate);
    }

    public List<FacilityBooking> findByCustomer(int customerId) {
        String sql = "SELECT * FROM facility_bookings WHERE customer_id = ?";
        return jdbcTemplate.query(sql, (rs, rn) -> map(rs), customerId);
    }

    public boolean updateStatus(int bookingId, String bookingStatus) {
        String sql = "UPDATE facility_bookings SET booking_status = ?, updated_at = CURRENT_TIMESTAMP WHERE facility_booking_id = ?";
        return jdbcTemplate.update(sql, bookingStatus, bookingId) > 0;
    }

    public boolean updatePaymentStatus(int bookingId, String paymentStatus) {
        String sql = "UPDATE facility_bookings SET payment_status = ?, updated_at = CURRENT_TIMESTAMP WHERE facility_booking_id = ?";
        return jdbcTemplate.update(sql, paymentStatus, bookingId) > 0;
    }

    public boolean delete(int bookingId) {
        String sql = "DELETE FROM facility_bookings WHERE facility_booking_id = ?";
        return jdbcTemplate.update(sql, bookingId) > 0;
    }
    
    public boolean isFacilityBookingPaid(int facilityBookingId) {
        String sql = "SELECT payment_status FROM facility_bookings WHERE facility_booking_id = ?";
        String status = jdbcTemplate.queryForObject(sql, String.class, facilityBookingId);
        return status != null && "PAID".equalsIgnoreCase(status);
    }
    
    public boolean existsByIdAndCustomerId(Integer facilityBookingId, Integer customerId) {
        String sql = "SELECT COUNT(1) FROM facility_bookings WHERE facility_booking_id = ? AND customer_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, facilityBookingId, customerId);
        return count != null && count > 0;
    }




    private FacilityBooking map(ResultSet rs) throws SQLException {
        FacilityBooking b = new FacilityBooking();
        b.setFacilityBookingId(rs.getInt("facility_booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setFacilityId(rs.getInt("facility_id"));
        b.setBookingDate(rs.getDate("booking_date") != null ? rs.getDate("booking_date").toLocalDate().toString() : null);
        b.setStartTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime().toString() : null);
        b.setEndTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime().toString() : null);
        b.setTimeSlot(rs.getString("time_slot"));
        b.setQuantity(rs.getInt("quantity"));
        b.setTotalPrice(rs.getDouble("total_price"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setBookingStatus(rs.getString("booking_status"));
        b.setNotes(rs.getString("notes"));
        b.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : null);
        b.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toString() : null);
        return b;
    }

    private FacilityBookingFullDTO mapFull(ResultSet rs) throws SQLException {
        FacilityBookingFullDTO dto = new FacilityBookingFullDTO();
        dto.setFacilityBookingId(rs.getInt("facility_booking_id"));
        dto.setCustomerId(rs.getInt("customer_id"));
        dto.setFacilityId(rs.getInt("facility_id"));
        dto.setBookingDate(rs.getDate("booking_date") != null ? rs.getDate("booking_date").toLocalDate().toString() : null);
        dto.setStartTime(rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime().toString() : null);
        dto.setEndTime(rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime().toString() : null);
        dto.setTimeSlot(rs.getString("time_slot"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setTotalPrice(rs.getDouble("total_price"));
        dto.setPaymentStatus(rs.getString("payment_status"));
        dto.setBookingStatus(rs.getString("booking_status"));
        dto.setNotes(rs.getString("notes"));
        dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : null);
        dto.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toString() : null);

        dto.setCustomerName(rs.getString("customer_name"));
        dto.setCustomerEmail(rs.getString("customer_email"));
        dto.setCustomerPhone(rs.getString("customer_phone"));

        dto.setFacilityName(rs.getString("facility_name"));
        dto.setFacilityType(rs.getString("facility_type"));
        dto.setFacilityLocation(rs.getString("facility_location"));
        int cap = rs.getInt("facility_capacity");
        dto.setFacilityCapacity(rs.wasNull() ? null : cap);

        int pid = rs.getInt("payment_id");
        dto.setPaymentId(rs.wasNull() ? null : pid);
        dto.setPaymentStatusDetail(rs.getString("payment_status_detail"));
        double pamt = rs.getDouble("payment_amount");
        dto.setPaymentAmount(rs.wasNull() ? null : pamt);

        return dto;
    }
}
