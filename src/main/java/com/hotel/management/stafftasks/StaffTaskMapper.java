package com.hotel.management.stafftasks;

public class StaffTaskMapper {

    public static StaffTaskDTO toDTO(StaffTask t) {
        if (t == null) return null;
        StaffTaskDTO dto = new StaffTaskDTO();
        dto.setTaskId(t.getTaskId());
        dto.setStaffId(t.getStaffId());
        dto.setRoomId(t.getRoomId());
        dto.setTaskType(t.getTaskType());
        dto.setStatus(t.getStatus());
        dto.setRemarks(t.getRemarks());
        dto.setAssignedAt(t.getAssignedAt());
        dto.setCompletedAt(t.getCompletedAt());

        // Enriched
        dto.setStaffName(t.getStaffName());
        dto.setStaffUserId(t.getStaffUserId());
        dto.setRoomNumber(t.getRoomNumber());

        return dto;
    }
        public static StaffTask toEntity(StaffTaskDTO dto) {
            if (dto == null) return null;
            StaffTask t = new StaffTask();
            if (dto.getTaskId() != null) t.setTaskId(dto.getTaskId());
            if (dto.getStaffId() != null) t.setStaffId(dto.getStaffId());
            t.setRoomId(dto.getRoomId());
            t.setTaskType(dto.getTaskType());
            t.setStatus(dto.getStatus());
            t.setRemarks(dto.getRemarks());
            t.setAssignedAt(dto.getAssignedAt());
            t.setCompletedAt(dto.getCompletedAt());
            // Note: staffName / roomNumber are read-only enrichment fields, not copied back.
            return t;
        }
    }