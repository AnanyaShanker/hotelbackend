package com.hotel.management.stafftasks;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffTaskRowMapper implements RowMapper<StaffTask> {
    @Override
    public StaffTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        StaffTask t = new StaffTask();
        t.setTaskId(rs.getInt("task_id"));
        t.setStaffId(rs.getInt("staff_id"));

        // room_id can be null
        Object roomIdObj = rs.getObject("room_id");
        t.setRoomId(roomIdObj != null ? rs.getInt("room_id") : null);

        t.setTaskType(rs.getString("task_type"));
        t.setStatus(rs.getString("status"));
        t.setAssignedAt(rs.getTimestamp("assigned_at"));
        t.setCompletedAt(rs.getTimestamp("completed_at"));
        t.setRemarks(rs.getString("remarks"));

        // optional joined columns (may be null)
        try {
            String staffName = rs.getString("staff_name");
            t.setStaffName(staffName);
        } catch (SQLException ignore) {}

        try {
            Object staffUserIdObj = rs.getObject("user_id");
            if (staffUserIdObj != null) t.setStaffUserId((Integer) staffUserIdObj);
        } catch (SQLException ignore) {}

        try {
            String roomNumber = rs.getString("room_number");
            t.setRoomNumber(roomNumber);
        } catch (SQLException ignore) {}

        return t;
    }
}