package com.hotel.management.stafftasks;


import com.hotel.management.notifications.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StaffTaskCreatedListener {

    private final NotificationService notificationService;

    public StaffTaskCreatedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @EventListener
    public void handleStaffTaskCreated(StaffTaskCreatedEvent event) {
        try {
            StaffTaskDTO task = event.getTaskDTO();
            Integer staffUserId = event.getStaffUserId();
            String roomNumber = event.getRoomNumber();

            // Build notification data
            Map<String, Object> data = new HashMap<>();
            data.put("task", task.getTaskType());
            data.put("roomNumber", roomNumber != null ? roomNumber : "N/A");
            data.put("taskId", task.getTaskId());
            data.put("remarks", task.getRemarks() != null ? task.getRemarks() : "No remarks");

            // Send notification
            notificationService.createEventNotification(
                staffUserId,
                "STAFF_TASK_ASSIGNED",
                data
            );

            System.out.println(" Task notification sent to staff user ID: " + staffUserId);

        } catch (Exception e) {
            System.err.println(" Failed to send task notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

