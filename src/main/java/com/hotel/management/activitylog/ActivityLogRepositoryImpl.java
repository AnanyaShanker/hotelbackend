package com.hotel.management.activitylog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@SuppressWarnings("unused")
public class ActivityLogRepositoryImpl implements ActivityLogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(ActivityLog log) {
        String sql = "INSERT INTO activity_log (user_id, action, entity_type, entity_id, description, " +
                     "ip_address, device_info, user_agent, request_method, endpoint, status_code, " +
                     "error_message, execution_time_ms) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            log.getUserId(),
            log.getAction(),
            log.getEntityType(),
            log.getEntityId(),
            log.getDescription(),
            log.getIpAddress(),
            log.getDeviceInfo(),
            log.getUserAgent(),
            log.getRequestMethod(),
            log.getEndpoint(),
            log.getStatusCode(),
            log.getErrorMessage(),
            log.getExecutionTimeMs()
        );
    }

    @Override
    public List<ActivityLog> findAll() {
        String sql = "SELECT * FROM activity_log ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new ActivityLogRowMapper());
    }

    @Override
    public List<ActivityLogWithUser> findAllWithUserDetails() {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper());
    }

    @Override
    public List<ActivityLogWithUser> findByUserIdWithUserDetails(Integer userId) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.user_id = ? " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), userId);
    }

    @Override
    public List<ActivityLogWithUser> findByAction(String action) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.action LIKE ? " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), "%" + action + "%");
    }

    @Override
    public List<ActivityLogWithUser> findByEntity(String entityType, Integer entityId) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.entity_type = ? AND a.entity_id = ? " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), entityType, entityId);
    }

    @Override
    public List<ActivityLogWithUser> findByDateRange(Timestamp start, Timestamp end) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.created_at BETWEEN ? AND ? " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), start, end);
    }

    @Override
    public List<ActivityLogWithUser> findRecentLogs(int limit) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "ORDER BY a.created_at DESC " +
                     "LIMIT ?";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), limit);
    }

    @Override
    public List<ActivityLogWithUser> findByIpAddress(String ipAddress) {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.ip_address = ? " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper(), ipAddress);
    }

    @Override
    public List<ActivityLogWithUser> findFailedRequests() {
        String sql = "SELECT a.log_id, a.user_id, u.name as user_name, u.email as user_email, " +
                     "u.status as user_status, u.phone as user_phone, a.action, a.entity_type, a.entity_id, " +
                     "a.description, a.ip_address, a.device_info, a.user_agent, a.request_method, " +
                     "a.endpoint, a.status_code, a.error_message, a.execution_time_ms, a.created_at " +
                     "FROM activity_log a " +
                     "LEFT JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.status_code >= 400 OR a.error_message IS NOT NULL " +
                     "ORDER BY a.created_at DESC";

        return jdbcTemplate.query(sql, new ActivityLogWithUserRowMapper());
    }

    @Override
    public int countByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM activity_log WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }
}

