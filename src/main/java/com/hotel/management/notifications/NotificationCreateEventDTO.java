package com.hotel.management.notifications;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class NotificationCreateEventDTO {
    @NotBlank
    private String eventType; // e.g. BOOKING_CONFIRMATION, STAFF_TASK_ASSIGNED

    @NotNull
    private Integer recipientUserId;

    @NotNull
    private Map<String, Object> data;

    // getters / setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Integer recipientUserId) { this.recipientUserId = recipientUserId; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
