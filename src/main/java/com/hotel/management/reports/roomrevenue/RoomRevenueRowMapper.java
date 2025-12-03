package com.hotel.management.reports.roomrevenue;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class RoomRevenueRowMapper implements RowMapper<RoomRevenueDto> {

    @Override
    public RoomRevenueDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        String typeName = rs.getString("type_name");
        BigDecimal revenue = rs.getBigDecimal("revenue");
        if (revenue == null) revenue = BigDecimal.ZERO;
        Integer totalBookings = rs.getInt("total_bookings");
        if (rs.wasNull()) totalBookings = 0;
        Integer bookedNights = rs.getInt("booked_nights");
        if (rs.wasNull()) bookedNights = 0;
        BigDecimal avg = rs.getBigDecimal("avg_revenue");
        if (avg == null) avg = BigDecimal.ZERO;

        return new RoomRevenueDto(typeName, revenue, totalBookings, bookedNights, avg);
    }
}

