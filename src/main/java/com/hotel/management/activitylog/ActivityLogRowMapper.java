package com.hotel.management.activitylog;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityLogRowMapper implements RowMapper<ActivityLog> {

    @Override
    public ActivityLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId((Integer) rs.getObject("user_id"));
        log.setAction(rs.getString("action"));
        log.setEntityType(rs.getString("entity_type"));
        log.setEntityId((Integer) rs.getObject("entity_id"));
        log.setDescription(rs.getString("description"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setDeviceInfo(rs.getString("device_info"));
        log.setUserAgent(rs.getString("user_agent"));
        log.setRequestMethod(rs.getString("request_method"));
        log.setEndpoint(rs.getString("endpoint"));
        log.setStatusCode((Integer) rs.getObject("status_code"));
        log.setErrorMessage(rs.getString("error_message"));
        log.setExecutionTimeMs((Long) rs.getObject("execution_time_ms"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }
}

