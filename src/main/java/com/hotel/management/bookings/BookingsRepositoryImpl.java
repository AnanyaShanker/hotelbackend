package com.hotel.management.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class BookingsRepositoryImpl implements BookingsRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

   

    @Override
    public Bookings save(Bookings booking) {
        String sql = "INSERT INTO bookings " +
                "(customer_id, room_id, branch_id, check_in_date, check_out_date, booking_status, notes) " +
                "VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                booking.getCustomerId(),
                booking.getRoomId(),
                booking.getBranchId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getBookingStatus(),
                booking.getNotes()
        );
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
    public boolean existsByIdAndCustomerId(Integer bookingId, Integer customerId) {
        String sql = "SELECT COUNT(1) FROM bookings WHERE booking_id = ? AND customer_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bookingId, customerId);
        return count != null && count > 0;
    }

}
