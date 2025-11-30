package com.hotel.management.notifications;

import com.hotel.management.notifications.NotificationCreateEventDTO;
import com.hotel.management.notifications.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) { this.service = service; }

    @PostMapping("/event")
    public ResponseEntity<?> createEventNotification(@Valid @RequestBody NotificationCreateEventDTO dto) {
        try {
            Integer id = service.createEventNotification(dto.getRecipientUserId(), dto.getEventType(), dto.getData());
            return ResponseEntity.ok().body("{\"notificationId\": " + id + "}");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\":\"" + ex.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Integer id) {
        boolean ok = service.softDelete(id);
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
