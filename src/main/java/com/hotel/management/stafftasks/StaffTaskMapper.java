package com.hotel.management.stafftasks;

public class StaffTaskMapper {
    public static StaffTaskDTO toDTO(StaffTask t) {
        StaffTaskDTO dto = new StaffTaskDTO();
        dto.setTaskId(t.getTaskId());
        dto.setStaffId(t.getStaffId());
        dto.setRoomId(t.getRoomId());
        dto.setTaskType(t.getTaskType());
        dto.setStatus(t.getStatus());
        dto.setRemarks(t.getRemarks());
        return dto;
    }

    public static StaffTask toEntity(StaffTaskDTO dto) {
        StaffTask t = new StaffTask();
       
        t.setStaffId(dto.getStaffId());
        t.setRoomId(dto.getRoomId());
        t.setTaskType(dto.getTaskType());
        t.setStatus(dto.getStatus());
        t.setRemarks(dto.getRemarks());
        return t;
    }
}
