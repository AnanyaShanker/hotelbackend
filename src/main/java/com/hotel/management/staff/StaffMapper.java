package com.hotel.management.staff;

public class StaffMapper {
    public static StaffDTO toDTO(Staff s) {
        StaffDTO dto = new StaffDTO();
        dto.setStaffId(s.getStaffId());
        dto.setUserId(s.getUserId());
        dto.setHotelId(s.getHotelId());
        dto.setStatus(s.getStatus());
        return dto;
    }

    public static Staff toStaff(StaffDTO dto) {
        Staff s = new Staff();
        s.setStaffId(dto.getStaffId());
        s.setUserId(dto.getUserId());
        s.setHotelId(dto.getHotelId());
        s.setStatus(dto.getStatus());
        return s;
    }
}
