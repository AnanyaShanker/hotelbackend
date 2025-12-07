package com.hotel.management.staff;

public class StaffMapper {
    public static StaffDTO toDTO(Staff s) {
        StaffDTO dto = new StaffDTO();
        dto.setStaffId(s.getStaffId());
        dto.setUserId(s.getUserId());
        dto.setHotelId(s.getHotelId());
        dto.setStatus(s.getStatus());

        // Map additional fields
        dto.setStaffIdentifier(s.getStaffIdentifier());
        dto.setDepartment(s.getDepartment());
        dto.setHireDate(s.getHireDate());
        dto.setReportsTo(s.getReportsTo());

        return dto;
    }

    public static Staff toStaff(StaffDTO dto) {
        Staff s = new Staff();
        s.setStaffId(dto.getStaffId());
        s.setUserId(dto.getUserId());
        s.setHotelId(dto.getHotelId());
        s.setStatus(dto.getStatus());

        // Map additional fields
        s.setStaffIdentifier(dto.getStaffIdentifier());
        s.setDepartment(dto.getDepartment());
        s.setHireDate(dto.getHireDate());
        s.setReportsTo(dto.getReportsTo());

        return s;
    }
}
