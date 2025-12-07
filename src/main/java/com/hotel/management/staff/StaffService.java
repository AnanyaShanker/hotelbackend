	package com.hotel.management.staff;

import java.util.List;

public interface StaffService {
    StaffDTO createStaff(Integer userId, Integer hotelId);
    StaffDTO getById(int staffId);
    StaffDTO getByUserId(int userId);
    List<StaffDTO> list(Integer hotelId, String status);
    StaffDTO updateHotel(int staffId, int hotelId);
    StaffDTO updateStatus(int staffId, String status);

    // Get staff by staff_identifier (e.g., ST-MUM-001)
    StaffDTO getStaffByStaffId(String staffIdentifier);
}
