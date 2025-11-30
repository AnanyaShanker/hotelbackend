package com.hotel.management.notifications;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.management.users.UserDTO;
import com.hotel.management.users.UserService;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationEngine engine;
    private final EmailService emailService;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationEngine engine,
                               EmailService emailService,
                               UserService userService) {
        this.notificationRepository = notificationRepository;
        this.engine = engine;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional
    public Integer createEventNotification(Integer recipientUserId, String eventType, Map<String, Object> data) throws Exception {
    	Map<String, String> emailParts =
    		    engine.buildEmailParts(recipientUserId,
    		                           NotificationType.valueOf(eventType),
    		                           data);

        String subject = emailParts.get("subject");
        String htmlBody = emailParts.get("body");

        String fallback = data.containsKey("message") ? data.get("message").toString() : subject;

        Integer id = notificationRepository.save(recipientUserId, eventType, fallback, "EMAIL");

        UserDTO user = userService.getUserById(recipientUserId);
        if (user == null) {
            notificationRepository.updateStatus(id, "FAILED");
            throw new IllegalStateException("Recipient user not found");
        }

        boolean sent = emailService.sendHtmlEmail(user.getEmail(), subject, htmlBody);
        notificationRepository.updateStatus(id, sent ? "SENT" : "FAILED");

        return id;
    }
    
    public Map<String, String> buildEmailParts(Integer recipientUserId,
            NotificationType type,
            Map<String, Object> data) throws Exception {
return engine.buildEmailParts(recipientUserId, type, data);
}


    public boolean softDelete(Integer id) {
        return notificationRepository.softDelete(id);
    }

    
    @Transactional
    public Integer sendNotification(Integer recipientUserId, NotificationType type, Map<String, Object> data) throws Exception {
        return createEventNotification(recipientUserId, type.name(), data);
    }
}
