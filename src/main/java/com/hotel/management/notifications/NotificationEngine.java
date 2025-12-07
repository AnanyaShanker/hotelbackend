package com.hotel.management.notifications;

import com.hotel.management.users.UserDTO;
import com.hotel.management.users.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationEngine {

    private final TemplateRenderer templateRenderer;
    private final UserService userService;

    public NotificationEngine(TemplateRenderer templateRenderer, UserService userService) {
        this.templateRenderer = templateRenderer;
        this.userService = userService;
    }

    /**
     * returns map with keys "subject" and "body"
     */
    public Map<String, String> buildEmailParts(Integer userId, NotificationType eventType, Map<String, Object> data) throws Exception {
        UserDTO user = userService.getUserById(userId);
        if (user == null) throw new IllegalArgumentException("User not found: " + userId);

        Map<String, Object> model = new HashMap<>(data);
        model.put("userName", user.getName());

        String templateFile;
        String subject;

        switch (eventType) {
        case USER_CREATED:
            templateFile = "user_welcome.html";
            subject = "Welcome to the Hotel System!";
            model.put("email", user.getEmail());
            break;

        case BOOKING_CONFIRMATION:
            templateFile = "booking_confirmation_customer.html";
            subject = "Booking Confirmed - " + data.getOrDefault("bookingId", "");
            model.put("bookingId", data.getOrDefault("bookingId", ""));
            model.put("checkInDate", data.getOrDefault("checkInDate", ""));
            break;

        case PAYMENT_RECEIVED:
            templateFile = "payment_received_customer.html";
            subject = "Payment Confirmation - Booking #" + data.getOrDefault("bookingId", "");
            model.put("customerName", user.getName()); // Add customer name for email template
            model.put("amount", data.getOrDefault("amount", ""));
            model.put("bookingId", data.getOrDefault("bookingId", ""));
            model.put("transactionId", data.getOrDefault("transactionId", "N/A"));
            model.put("paymentMethod", data.getOrDefault("paymentMethod", "Online"));
            // Format current date for payment date
            model.put("paymentDate", java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            break;

        case STAFF_TASK_ASSIGNED:
            templateFile = "staff_task_assigned.html";
            subject = "New Task Assigned - Room " + data.getOrDefault("roomNumber", "");
            model.put("task", data.getOrDefault("task", ""));
            model.put("roomNumber", data.getOrDefault("roomNumber", ""));
            break;

        case REVENUE_REPORT:
            templateFile = "revenue_report.html";
            subject = "Daily Revenue Report";
            model.put("roomRevenue", data.getOrDefault("roomRevenue", "0"));
            model.put("facilityRevenue", data.getOrDefault("facilityRevenue", "0"));
            model.put("total", data.getOrDefault("total", "0"));
            break;

        case FACILITY_BOOKING_CONFIRMATION:
            templateFile = "facility_booking_confirmation_customer.html";
            subject = "Facility Booking Confirmed - " + data.getOrDefault("bookingId", "");
            model.put("bookingId", data.getOrDefault("bookingId", ""));
            model.put("facilityName", data.getOrDefault("facilityName", ""));
            break;

        case DAILY_SUMMARY:
            templateFile = "manager_daily_summary.html";
            subject = "Daily Manager Summary - " + data.getOrDefault("date", "");
            model.put("date", data.getOrDefault("date", ""));
            model.put("totalBookings", data.getOrDefault("totalBookings", "0"));
            model.put("checkedIn", data.getOrDefault("checkedIn", "0"));
            model.put("checkedOut", data.getOrDefault("checkedOut", "0"));
            model.put("facilityBookings", data.getOrDefault("facilityBookings", "0"));
            model.put("topFacility", data.getOrDefault("topFacility", "N/A"));
            model.put("staffTasks", data.getOrDefault("staffTasks", "0"));
            model.put("pendingMaintenance", data.getOrDefault("pendingMaintenance", "0"));
            break;

        default:
            templateFile = "generic_notification.html";
            subject = "Notification";
            model.put("message", data.getOrDefault("message", ""));
    }


        String body = templateRenderer.render(templateFile, model);

        Map<String, String> out = new HashMap<>();
        out.put("subject", subject);
        out.put("body", body);
        return out;
    }
}
