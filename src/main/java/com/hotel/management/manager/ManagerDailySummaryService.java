//package com.hotel.management.manager;
//
//
//import com.hotel.management.notifications.NotificationService;
//import com.hotel.management.branches.BranchRepository;
//import com.hotel.management.branches.Branch;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Service to send daily summary emails to managers
// */
//@Service
//public class ManagerDailySummaryService {
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private ManagerDashboardService dashboardService;
//
//    @Autowired
//    private BranchRepository branchRepository;
//
//    /**
//     * Send daily summary to a specific manager
//     * Can be called manually via API endpoint
//     */
//    public void sendDailySummaryToManager(Integer managerId, Integer branchId) {
//        try {
//            // Get today's stats
//            ManagerDashboardDTO.TodayStats stats = dashboardService.getTodayStats(branchId);
//
//            // Get branch info
//            Branch branch = branchRepository.findById(branchId).orElse(null);
//            String branchName = branch != null ? branch.getName() : "Your Branch";
//
//            // Prepare data
//            Map<String, Object> data = new HashMap<>();
//            data.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
//            data.put("branchName", branchName);
//            data.put("totalBookings", stats.getTotalBookings());
//            data.put("checkedIn", stats.getPendingCheckIns());
//            data.put("checkedOut", stats.getPendingCheckOuts());
//            data.put("facilityBookings", "N/A"); // Can be added if needed
//            data.put("topFacility", "N/A"); // Can be added if needed
//            data.put("staffTasks", "N/A"); // Can be added if needed
//            data.put("pendingMaintenance", "N/A"); // Can be added if needed
//
//            // Send notification
//            notificationService.createEventNotification(
//                managerId,
//                "DAILY_SUMMARY",
//                data
//            );
//
//            System.out.println("✅ Daily summary sent to manager ID: " + managerId + " for branch: " + branchId);
//
//        } catch (Exception e) {
//            System.err.println("❌ Failed to send daily summary to manager " + managerId + ": " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Send daily summary to all managers
//     * Scheduled to run every day at 6 PM
//     * Uncomment @Scheduled annotation to enable
//     */
//    // @Scheduled(cron = "0 0 18 * * *") // 6 PM daily
//    public void sendDailySummaryToAllManagers() {
//        try {
//            List<Branch> branches = branchRepository.findAll();
//
//            for (Branch branch : branches) {
//                if (branch.getManagerId() != null) {
//                    sendDailySummaryToManager(branch.getManagerId(), branch.getBranchId());
//                }
//            }
//
//            System.out.println(" Daily summaries sent to all managers");
//        } catch (Exception e) {
//            System.err.println(" Failed to send daily summaries: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
//
