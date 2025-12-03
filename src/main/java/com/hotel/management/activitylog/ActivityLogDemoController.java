package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo Controller to test and visualize activity logging
 * Access at: http://localhost:8080/api/activity-demo
 */
@RestController
@RequestMapping("/api/activity-demo")
@SuppressWarnings("unused")
public class ActivityLogDemoController {

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private ActivityLogHelper activityLogHelper;

    /**
     * Test endpoint to see what gets logged automatically
     * Try: GET http://localhost:8080/api/activity-demo/test
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testAutoLogging(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "This request will be automatically logged!");
        response.put("userId", userId);
        response.put("endpoint", request.getRequestURI());
        response.put("method", request.getMethod());
        response.put("ipAddress", request.getRemoteAddr());
        response.put("userAgent", request.getHeader("User-Agent"));

        return ResponseEntity.ok(response);
    }

    /**
     * Test manual logging
     * Try: POST http://localhost:8080/api/activity-demo/manual-log
     */
    @PostMapping("/manual-log")
    public ResponseEntity<String> testManualLogging(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        // This will create an additional log entry (besides the automatic one)
        activityLogService.logActivity(
            userId,
            "DEMO_MANUAL_LOG",
            "Demo",
            999,
            "This is a manually logged activity for demonstration",
            request
        );

        return ResponseEntity.ok("Manual log created! Check /api/activity-logs/recent");
    }

    /**
     * Simulate booking creation with logging
     * Try: POST http://localhost:8080/api/activity-demo/simulate-booking
     */
    @PostMapping("/simulate-booking")
    public ResponseEntity<Map<String, Object>> simulateBookingCreation(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        // Simulate creating a booking
        Integer fakeBookingId = 123;

        // Log the business event
        activityLogHelper.logBookingCreated(userId, fakeBookingId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Simulated booking creation");
        response.put("bookingId", fakeBookingId);
        response.put("logged", "Two entries: 1) Automatic HTTP request, 2) Manual business event");

        return ResponseEntity.ok(response);
    }

    /**
     * Simulate various activities
     * Try: POST http://localhost:8080/api/activity-demo/simulate-activities
     */
    @PostMapping("/simulate-activities")
    public ResponseEntity<Map<String, Object>> simulateMultipleActivities(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        // Simulate different activities
        activityLogHelper.logBookingCreated(userId, 101, request);
        activityLogHelper.logRoomStatusChange(userId, 201, "MAINTENANCE", request);
        activityLogHelper.logPaymentReceived(userId, 301, 250.00, request);
        activityLogHelper.logTicketCreated(userId, 401, "AC not working", request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Simulated 4 different activities");
        response.put("activities", List.of(
            "Booking created (ID: 101)",
            "Room status changed (ID: 201)",
            "Payment received ($250.00)",
            "Support ticket created (ID: 401)"
        ));
        response.put("checkLogs", "GET /api/activity-logs/recent");

        return ResponseEntity.ok(response);
    }

    /**
     * View what got logged for the current user
     * Try: GET http://localhost:8080/api/activity-demo/my-logs
     */
    @GetMapping("/my-logs")
    public ResponseEntity<List<ActivityLogWithUser>> viewMyLogs(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<ActivityLogWithUser> myLogs = activityLogService.getUserActivityHistory(userId);
        return ResponseEntity.ok(myLogs);
    }

    /**
     * Simulate an error to see error logging
     * Try: GET http://localhost:8080/api/activity-demo/simulate-error
     */
    @GetMapping("/simulate-error")
    public ResponseEntity<String> simulateError() {
        // This will throw an exception and be logged
        throw new RuntimeException("This is a simulated error for testing activity logging!");
    }

    /**
     * Test slow request to see execution time logging
     * Try: GET http://localhost:8080/api/activity-demo/slow-request
     */
    @GetMapping("/slow-request")
    public ResponseEntity<Map<String, Object>> slowRequest() throws InterruptedException {
        // Simulate slow operation
        Thread.sleep(2000); // 2 seconds

        Map<String, Object> response = new HashMap<>();
        response.put("message", "This took 2 seconds");
        response.put("checkLog", "Check execution_time_ms in activity_log table");

        return ResponseEntity.ok(response);
    }

    /**
     * Get statistics about logged activities
     * Try: GET http://localhost:8080/api/activity-demo/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<ActivityLogWithUser> allLogs = activityLogService.getRecentActivities(100);
        List<ActivityLogWithUser> failures = activityLogService.getFailedRequests();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecentActivities", allLogs.size());
        stats.put("failedRequests", failures.size());
        stats.put("sampleRecentActivities", allLogs.size() > 5 ? allLogs.subList(0, 5) : allLogs);

        return ResponseEntity.ok(stats);
    }
}

