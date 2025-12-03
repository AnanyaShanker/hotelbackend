package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

public interface ActivityLogService {

    /**
     * Log an activity manually
     */
    void logActivity(Integer userId, String action, String entityType, Integer entityId,
                    String description, HttpServletRequest request);

    /**
     * Log activity with minimal information
     */
    void logSimple(Integer userId, String action, String description, HttpServletRequest request);

    /**
     * Get all activity logs with user details
     */
    List<ActivityLogWithUser> getAllActivities();

    /**
     * Get user's activity history
     */
    List<ActivityLogWithUser> getUserActivityHistory(Integer userId);

    /**
     * Get entity history (e.g., all activities related to a specific booking)
     */
    List<ActivityLogWithUser> getEntityHistory(String entityType, Integer entityId);

    /**
     * Get activities by date range
     */
    List<ActivityLogWithUser> getActivitiesByDateRange(Timestamp start, Timestamp end);

    /**
     * Get recent activities
     */
    List<ActivityLogWithUser> getRecentActivities(int limit);

    /**
     * Get activities by IP address
     */
    List<ActivityLogWithUser> getActivitiesByIpAddress(String ipAddress);

    /**
     * Get failed requests
     */
    List<ActivityLogWithUser> getFailedRequests();

    /**
     * Get activity count for a user
     */
    int getUserActivityCount(Integer userId);
}

