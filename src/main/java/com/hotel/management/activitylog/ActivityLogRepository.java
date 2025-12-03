package com.hotel.management.activitylog;

import java.sql.Timestamp;
import java.util.List;

public interface ActivityLogRepository {

    /**
     * Save a new activity log entry
     */
    void save(ActivityLog log);

    /**
     * Find all activity logs
     */
    List<ActivityLog> findAll();

    /**
     * Find all activity logs with user details (JOIN query)
     */
    List<ActivityLogWithUser> findAllWithUserDetails();

    /**
     * Find activity logs by user ID with user details
     */
    List<ActivityLogWithUser> findByUserIdWithUserDetails(Integer userId);

    /**
     * Find activity logs by action
     */
    List<ActivityLogWithUser> findByAction(String action);

    /**
     * Find activity logs by entity type and entity ID
     */
    List<ActivityLogWithUser> findByEntity(String entityType, Integer entityId);

    /**
     * Find activity logs within a date range
     */
    List<ActivityLogWithUser> findByDateRange(Timestamp start, Timestamp end);

    /**
     * Find recent activity logs (limited)
     */
    List<ActivityLogWithUser> findRecentLogs(int limit);

    /**
     * Find activity logs by IP address
     */
    List<ActivityLogWithUser> findByIpAddress(String ipAddress);

    /**
     * Find failed requests (status code >= 400)
     */
    List<ActivityLogWithUser> findFailedRequests();

    /**
     * Get activity count by user
     */
    int countByUserId(Integer userId);
}

