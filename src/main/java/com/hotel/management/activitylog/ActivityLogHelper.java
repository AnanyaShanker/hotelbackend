package com.hotel.management.activitylog;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to easily integrate activity logging into your existing services
 *
 * Usage Example in any Service:
 *
 * @Autowired
 * private ActivityLogHelper activityLogHelper;
 *
 * // Then in your methods:
 * activityLogHelper.logBookingCreated(userId, bookingId, request);
 * activityLogHelper.logRoomStatusChange(userId, roomId, newStatus, request);
 */
@Component
@SuppressWarnings("unused")
public class ActivityLogHelper {

    @Autowired
    private ActivityLogService activityLogService;

    // ==================== BOOKING ACTIVITIES ====================

    public void logBookingCreated(Integer userId, Integer bookingId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CREATE_BOOKING",
            "Booking",
            bookingId,
            "New booking created",
            request
        );
    }

    public void logBookingUpdated(Integer userId, Integer bookingId, String details, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "UPDATE_BOOKING",
            "Booking",
            bookingId,
            "Booking updated: " + details,
            request
        );
    }

    public void logBookingCancelled(Integer userId, Integer bookingId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CANCEL_BOOKING",
            "Booking",
            bookingId,
            "Booking cancelled",
            request
        );
    }

    public void logBookingCompleted(Integer userId, Integer bookingId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "COMPLETE_BOOKING",
            "Booking",
            bookingId,
            "Booking completed",
            request
        );
    }

    // ==================== ROOM ACTIVITIES ====================

    public void logRoomCreated(Integer userId, Integer roomId, String roomNumber, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CREATE_ROOM",
            "Room",
            roomId,
            "Created room: " + roomNumber,
            request
        );
    }

    public void logRoomStatusChange(Integer userId, Integer roomId, String newStatus, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "UPDATE_ROOM_STATUS",
            "Room",
            roomId,
            "Room status changed to: " + newStatus,
            request
        );
    }

    public void logRoomCleaned(Integer staffId, Integer roomId, HttpServletRequest request) {
        activityLogService.logActivity(
            staffId,
            "CLEAN_ROOM",
            "Room",
            roomId,
            "Room cleaned by staff",
            request
        );
    }

    // ==================== USER ACTIVITIES ====================

    public void logUserCreated(Integer adminId, Integer newUserId, String userName, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "CREATE_USER",
            "User",
            newUserId,
            "Created new user: " + userName,
            request
        );
    }

    public void logUserUpdated(Integer userId, String details, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "UPDATE_USER",
            "User",
            userId,
            "User profile updated: " + details,
            request
        );
    }

    public void logUserStatusChange(Integer adminId, Integer userId, String newStatus, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "CHANGE_USER_STATUS",
            "User",
            userId,
            "User status changed to: " + newStatus,
            request
        );
    }

    public void logPasswordChanged(Integer userId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CHANGE_PASSWORD",
            "User",
            userId,
            "Password changed",
            request
        );
    }

    public void logLoginAttempt(Integer userId, boolean success, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            success ? "LOGIN_SUCCESS" : "LOGIN_FAILED",
            "Auth",
            userId,
            success ? "User logged in successfully" : "Failed login attempt",
            request
        );
    }

    public void logLogout(Integer userId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "LOGOUT",
            "Auth",
            userId,
            "User logged out",
            request
        );
    }

    // ==================== PAYMENT ACTIVITIES ====================

    public void logPaymentReceived(Integer userId, Integer paymentId, Double amount, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "PAYMENT_RECEIVED",
            "Payment",
            paymentId,
            "Payment received: $" + amount,
            request
        );
    }

    public void logPaymentFailed(Integer userId, Integer bookingId, String reason, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "PAYMENT_FAILED",
            "Payment",
            bookingId,
            "Payment failed: " + reason,
            request
        );
    }

    public void logRefundProcessed(Integer adminId, Integer paymentId, Double amount, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "REFUND_PROCESSED",
            "Payment",
            paymentId,
            "Refund processed: $" + amount,
            request
        );
    }

    // ==================== FACILITY ACTIVITIES ====================

    public void logFacilityBookingCreated(Integer userId, Integer facilityBookingId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CREATE_FACILITY_BOOKING",
            "FacilityBooking",
            facilityBookingId,
            "Facility booking created",
            request
        );
    }

    public void logFacilityBookingCancelled(Integer userId, Integer facilityBookingId, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CANCEL_FACILITY_BOOKING",
            "FacilityBooking",
            facilityBookingId,
            "Facility booking cancelled",
            request
        );
    }

    // ==================== SUPPORT TICKET ACTIVITIES ====================

    public void logTicketCreated(Integer userId, Integer ticketId, String subject, HttpServletRequest request) {
        activityLogService.logActivity(
            userId,
            "CREATE_TICKET",
            "SupportTicket",
            ticketId,
            "Support ticket created: " + subject,
            request
        );
    }

    public void logTicketAssigned(Integer adminId, Integer ticketId, Integer staffId, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "ASSIGN_TICKET",
            "SupportTicket",
            ticketId,
            "Ticket assigned to staff ID: " + staffId,
            request
        );
    }

    public void logTicketResolved(Integer staffId, Integer ticketId, HttpServletRequest request) {
        activityLogService.logActivity(
            staffId,
            "RESOLVE_TICKET",
            "SupportTicket",
            ticketId,
            "Support ticket resolved",
            request
        );
    }

    // ==================== STAFF TASK ACTIVITIES ====================

    public void logTaskAssigned(Integer managerId, Integer taskId, Integer staffId, HttpServletRequest request) {
        activityLogService.logActivity(
            managerId,
            "ASSIGN_TASK",
            "StaffTask",
            taskId,
            "Task assigned to staff ID: " + staffId,
            request
        );
    }

    public void logTaskCompleted(Integer staffId, Integer taskId, HttpServletRequest request) {
        activityLogService.logActivity(
            staffId,
            "COMPLETE_TASK",
            "StaffTask",
            taskId,
            "Task completed by staff",
            request
        );
    }

    // ==================== BRANCH ACTIVITIES ====================

    public void logBranchCreated(Integer adminId, Integer branchId, String branchName, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "CREATE_BRANCH",
            "Branch",
            branchId,
            "Created new branch: " + branchName,
            request
        );
    }

    public void logBranchUpdated(Integer adminId, Integer branchId, String details, HttpServletRequest request) {
        activityLogService.logActivity(
            adminId,
            "UPDATE_BRANCH",
            "Branch",
            branchId,
            "Branch updated: " + details,
            request
        );
    }

    // ==================== CUSTOM ACTIVITY ====================

    public void logCustomActivity(Integer userId, String action, String entityType, Integer entityId,
                                  String description, HttpServletRequest request) {
        activityLogService.logActivity(userId, action, entityType, entityId, description, request);
    }

    public void logSimpleActivity(Integer userId, String action, String description, HttpServletRequest request) {
        activityLogService.logSimple(userId, action, description, request);
    }
}

