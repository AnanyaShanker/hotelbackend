package com.hotel.management.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BookingsRepositoryImpl implements BookingsRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

   

    @Override
    public Bookings save(Bookings booking) {
        String sql = "INSERT INTO bookings " +
                "(customer_id, room_id, branch_id, check_in_date, check_out_date, payment_status, booking_status, notes, total_price) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, booking.getCustomerId());
            ps.setInt(2, booking.getRoomId());
            ps.setObject(3, booking.getBranchId());
            ps.setTimestamp(4, booking.getCheckInDate());
            ps.setTimestamp(5, booking.getCheckOutDate());
            ps.setString(6, booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "PENDING");
            ps.setString(7, booking.getBookingStatus());
            ps.setString(8, booking.getNotes());
            ps.setDouble(9, booking.getTotalPrice());
            return ps;
        }, keyHolder);

        // Set the generated ID back to the booking object
        int generatedId = keyHolder.getKey().intValue();
        booking.setBookingId(generatedId);

        return booking;
    }

    @Override
    public List<Bookings> findAll() {
        return jdbcTemplate.query("SELECT * FROM bookings", new BookingsRowMapper());
    }

    @Override
    public Bookings findById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, new BookingsRowMapper(), bookingId);
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        jdbcTemplate.update("UPDATE bookings SET booking_status=? WHERE booking_id=?", status, bookingId);
    }

    @Override
    public List<Bookings> findByBranch(int branchId) {
        String sql = "SELECT * FROM bookings WHERE branch_id = ?";
        return jdbcTemplate.query(sql, new BookingsRowMapper(), branchId);
    }

    @Override
    public List<ManagerBookingViewDTO> findDetailedByBranch(int branchId) {

        String sql = """
            SELECT 
                b.booking_id,
                u.name AS customer_name,
                u.email AS customer_email,
                r.room_id,
                r.room_number,
                b.check_in_date,
                b.check_out_date,
                b.booking_status
            FROM bookings b
            JOIN users u ON b.customer_id = u.user_id
            JOIN rooms r ON b.room_id = r.room_id
            WHERE b.branch_id = ?
            ORDER BY b.check_in_date DESC
        """;

        return jdbcTemplate.query(sql, (rs, n) -> {
            ManagerBookingViewDTO dto = new ManagerBookingViewDTO();
            dto.setBookingId(rs.getInt("booking_id"));
            dto.setCustomerName(rs.getString("customer_name"));
            dto.setCustomerEmail(rs.getString("customer_email"));
            dto.setRoomId(rs.getInt("room_id"));
            dto.setRoomNumber(rs.getString("room_number"));
            dto.setCheckInDate(rs.getTimestamp("check_in_date"));
            dto.setCheckOutDate(rs.getTimestamp("check_out_date"));
            dto.setBookingStatus(rs.getString("booking_status"));
            return dto;
        }, branchId);
    }
    @Override
    public List<Bookings> findByCustomer(int customerId) {
        String sql = "SELECT * FROM bookings WHERE customer_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BookingsRowMapper(), customerId);
    }

    @Override
    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        String sql = "UPDATE bookings SET payment_status = ? WHERE booking_id = ?";
        jdbcTemplate.update(sql, paymentStatus, bookingId);
    }

    @Override
    public boolean existsByIdAndCustomerId(Integer bookingId, Integer customerId) {
        String sql = "SELECT COUNT(1) FROM bookings WHERE booking_id = ? AND customer_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bookingId, customerId);
        return count != null && count > 0;
    }

}
