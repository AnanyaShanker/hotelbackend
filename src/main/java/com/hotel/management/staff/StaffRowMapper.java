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

        // Map additional fields (use try-catch in case columns don't exist yet)
        try {
            s.setStaffIdentifier(rs.getString("staff_identifier"));
        } catch (SQLException e) {
            s.setStaffIdentifier(null);
        }

        try {
            s.setDepartment(rs.getString("department"));
        } catch (SQLException e) {
            s.setDepartment(null);
        }

        try {
            s.setHireDate(rs.getDate("hire_date"));
        } catch (SQLException e) {
            s.setHireDate(null);
        }

        try {
            s.setReportsTo((Integer) rs.getObject("reports_to"));
        } catch (SQLException e) {
            s.setReportsTo(null);
        }

        return s;
    }
}
