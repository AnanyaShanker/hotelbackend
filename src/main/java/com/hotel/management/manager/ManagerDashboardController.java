//package com.hotel.management.manager;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/manager")
//public class ManagerDashboardController {
//
//    @Autowired
//    private ManagerDashboardService dashboardService;
//
//    /**
//     * Get complete dashboard data for a branch
//     * GET /api/manager/dashboard/{branchId}
//     */
//    @GetMapping("/dashboard/{branchId}")
//    public ResponseEntity<ManagerDashboardDTO> getDashboard(@PathVariable Integer branchId) {
//        ManagerDashboardDTO dashboard = dashboardService.getDashboardData(branchId);
//        return ResponseEntity.ok(dashboard);
//    }
//
//    /**
//     * Get today's statistics
//     * GET /api/manager/today-stats/{branchId}
//     */
//    @GetMapping("/today-stats/{branchId}")
//    public ResponseEntity<ManagerDashboardDTO.TodayStats> getTodayStats(@PathVariable Integer branchId) {
//        ManagerDashboardDTO.TodayStats stats = dashboardService.getTodayStats(branchId);
//        return ResponseEntity.ok(stats);
//    }
//
//    /**
//     * Get month statistics
//     * GET /api/manager/month-stats/{branchId}
//     */
//    @GetMapping("/month-stats/{branchId}")
//    public ResponseEntity<ManagerDashboardDTO.MonthStats> getMonthStats(@PathVariable Integer branchId) {
//        ManagerDashboardDTO.MonthStats stats = dashboardService.getMonthStats(branchId);
//        return ResponseEntity.ok(stats);
//    }
//
//    /**
//     * Get recent activity
//     * GET /api/manager/recent-activity/{branchId}?limit=10
//     */
//    @GetMapping("/recent-activity/{branchId}")
//    public ResponseEntity<List<ManagerDashboardDTO.RecentActivity>> getRecentActivity(
//            @PathVariable Integer branchId,
//            @RequestParam(defaultValue = "10") int limit) {
//        List<ManagerDashboardDTO.RecentActivity> activity = dashboardService.getRecentActivity(branchId, limit);
//        return ResponseEntity.ok(activity);
//    }
//
//    /**
//     * Get staff summary
//     * GET /api/manager/staff/{branchId}
//     */
//    @GetMapping("/staff/{branchId}")
//    public ResponseEntity<List<ManagerDashboardDTO.StaffSummary>> getStaffSummary(@PathVariable Integer branchId) {
//        List<ManagerDashboardDTO.StaffSummary> staff = dashboardService.getStaffSummary(branchId);
//        return ResponseEntity.ok(staff);
//    }
//
//    /**
//     * Get top performing rooms
//     * GET /api/manager/top-rooms/{branchId}?limit=5
//     */
//    @GetMapping("/top-rooms/{branchId}")
//    public ResponseEntity<List<ManagerDashboardDTO.RoomPerformance>> getTopRooms(
//            @PathVariable Integer branchId,
//            @RequestParam(defaultValue = "5") int limit) {
//        List<ManagerDashboardDTO.RoomPerformance> rooms = dashboardService.getTopPerformingRooms(branchId, limit);
//        return ResponseEntity.ok(rooms);
//    }
//
//    /**
//     * Get pending tasks
//     * GET /api/manager/pending-tasks/{branchId}
//     */
//    @GetMapping("/pending-tasks/{branchId}")
//    public ResponseEntity<List<ManagerDashboardDTO.PendingTask>> getPendingTasks(@PathVariable Integer branchId) {
//        List<ManagerDashboardDTO.PendingTask> tasks = dashboardService.getPendingTasks(branchId);
//        return ResponseEntity.ok(tasks);
//    }
//
//    /**
//     * Get branch information
//     * GET /api/manager/branch-info/{branchId}
//     */
//    @GetMapping("/branch-info/{branchId}")
//    public ResponseEntity<ManagerDashboardDTO.BranchInfo> getBranchInfo(@PathVariable Integer branchId) {
//        ManagerDashboardDTO.BranchInfo info = dashboardService.getBranchInfo(branchId);
//        return ResponseEntity.ok(info);
//    }
//
//    @Autowired
//    private ManagerDailySummaryService dailySummaryService;
//
//    /**
//     * Send daily summary email to manager
//     * POST /api/manager/send-daily-summary/{managerId}/{branchId}
//     */
//    @PostMapping("/send-daily-summary/{managerId}/{branchId}")
//    public ResponseEntity<String> sendDailySummary(
//            @PathVariable Integer managerId,
//            @PathVariable Integer branchId) {
//        dailySummaryService.sendDailySummaryToManager(managerId, branchId);
//        return ResponseEntity.ok("Daily summary email sent successfully");
//    }
//}
//
