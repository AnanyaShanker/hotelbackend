package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity-logs")
@SuppressWarnings("unused")
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    /**
     * Get all activity logs (Admin only)
     */
    @GetMapping
    public ResponseEntity<List<ActivityLogWithUser>> getAllLogs() {
        List<ActivityLogWithUser> logs = activityLogService.getAllActivities();
        return ResponseEntity.ok(logs);
    }

    /**
     * Get all activity logs - alias endpoint
     */
    @GetMapping("/all")
    public ResponseEntity<List<ActivityLogWithUser>> getAllLogsAlias() {
        List<ActivityLogWithUser> logs = activityLogService.getAllActivities();
        return ResponseEntity.ok(logs);
    }

    /**
     * Get recent activity logs with limit
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ActivityLogWithUser>> getRecentLogs(
            @RequestParam(defaultValue = "100") int limit) {
        List<ActivityLogWithUser> logs = activityLogService.getRecentActivities(limit);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get activity logs for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityLogWithUser>> getUserLogs(@PathVariable Integer userId) {
        List<ActivityLogWithUser> logs = activityLogService.getUserActivityHistory(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get activity logs for current logged-in user
     */
    @GetMapping("/my-activity")
    public ResponseEntity<List<ActivityLogWithUser>> getMyActivity(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<ActivityLogWithUser> logs = activityLogService.getUserActivityHistory(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get activity logs for a specific entity (e.g., booking, room)
     */
    @GetMapping("/entity")
    public ResponseEntity<List<ActivityLogWithUser>> getEntityLogs(
            @RequestParam String entityType,
            @RequestParam Integer entityId) {
        List<ActivityLogWithUser> logs = activityLogService.getEntityHistory(entityType, entityId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get activity logs by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ActivityLogWithUser>> getLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Timestamp start = Timestamp.valueOf(startDate + " 00:00:00");
            Timestamp end = Timestamp.valueOf(endDate + " 23:59:59");
            List<ActivityLogWithUser> logs = activityLogService.getActivitiesByDateRange(start, end);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get activity logs by IP address
     */
    @GetMapping("/ip/{ipAddress}")
    public ResponseEntity<List<ActivityLogWithUser>> getLogsByIp(@PathVariable String ipAddress) {
        List<ActivityLogWithUser> logs = activityLogService.getActivitiesByIpAddress(ipAddress);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get failed requests (errors and 4xx/5xx status codes)
     */
    @GetMapping("/failed")
    public ResponseEntity<List<ActivityLogWithUser>> getFailedRequests() {
        List<ActivityLogWithUser> logs = activityLogService.getFailedRequests();
        return ResponseEntity.ok(logs);
    }

    /**
     * Get activity statistics for a user
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Integer userId) {
        int count = activityLogService.getUserActivityCount(userId);
        List<ActivityLogWithUser> recentLogs = activityLogService.getUserActivityHistory(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActivities", count);
        stats.put("recentActivities", recentLogs.size() > 10 ? recentLogs.subList(0, 10) : recentLogs);

        return ResponseEntity.ok(stats);
    }

    /**
     * Manually log an activity (for testing or special cases)
     */
    @PostMapping("/log")
    public ResponseEntity<String> logActivity(
            @RequestParam String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Integer entityId,
            @RequestParam(required = false) String description,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("userId");
        activityLogService.logActivity(userId, action, entityType, entityId, description, request);

        return ResponseEntity.ok("Activity logged successfully");
    }
}

