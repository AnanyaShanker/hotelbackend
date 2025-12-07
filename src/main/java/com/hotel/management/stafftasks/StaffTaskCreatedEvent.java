package com.hotel.management.stafftasks;


import org.springframework.context.ApplicationEvent;

public class StaffTaskCreatedEvent extends ApplicationEvent {
    private final StaffTaskDTO taskDTO;
    private final Integer staffUserId;
    private final String roomNumber;

    public StaffTaskCreatedEvent(Object source, StaffTaskDTO taskDTO, Integer staffUserId, String roomNumber) {
        super(source);
        this.taskDTO = taskDTO;
        this.staffUserId = staffUserId;
        this.roomNumber = roomNumber;
    }

    public StaffTaskDTO getTaskDTO() {
        return taskDTO;
    }

    public Integer getStaffUserId() {
        return staffUserId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}



