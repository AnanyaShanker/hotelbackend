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
        t.setRoomId(rs.getInt("room_id"));
        t.setTaskType(rs.getString("task_type"));
        t.setStatus(rs.getString("status"));
        t.setAssignedAt(rs.getTimestamp("assigned_at"));
        t.setCompletedAt(rs.getTimestamp("completed_at"));
        t.setRemarks(rs.getString("remarks"));
        return t;
    }
}