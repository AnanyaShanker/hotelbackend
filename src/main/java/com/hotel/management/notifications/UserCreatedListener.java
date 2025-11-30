package com.hotel.management.notifications;

import com.hotel.management.users.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserCreatedListener {

    private final NotificationService notificationService;

    public UserCreatedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        try {
            notificationService.sendNotification(
                event.getUserId(),
                NotificationType.USER_CREATED,
                Map.of() 
            );
        } catch (Exception e) {
            // log error but don’t block the app
            e.printStackTrace();
        }
    }
}
