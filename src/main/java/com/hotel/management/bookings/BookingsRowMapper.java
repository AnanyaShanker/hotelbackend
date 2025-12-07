package com.hotel.management.bookings;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingsRowMapper implements RowMapper<Bookings> {
    @Override
    public Bookings mapRow(ResultSet rs, int rowNum) throws SQLException {
        Bookings b = new Bookings();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setBranchId((Integer) rs.getObject("branch_id"));
        b.setCheckInDate(rs.getTimestamp("check_in_date"));
        b.setCheckOutDate(rs.getTimestamp("check_out_date"));
        b.setTotalPrice(rs.getDouble("total_price"));
       
        b.setBookingStatus(rs.getString("booking_status"));
        b.setNotes(rs.getString("notes"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.setUpdatedAt(rs.getTimestamp("updated_at"));
        return b;
    }
}
