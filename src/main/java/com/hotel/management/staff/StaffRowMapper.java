package com.hotel.management.staff;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StaffRowMapper implements RowMapper<Staff> {
    @Override
    public Staff mapRow(ResultSet rs, int rowNum) throws SQLException {
        Staff s = new Staff();
        s.setStaffId(rs.getInt("staff_id"));
        s.setUserId(rs.getInt("user_id"));
        s.setHotelId(rs.getInt("hotel_id"));
        s.setStatus(rs.getString("status"));
        return s;
    }
}
