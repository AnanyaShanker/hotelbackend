package com.hotel.management.staff;

import java.util.List;

import java.util.Optional;

public interface StaffRepository {
    int save(Staff staff);
    Optional<Staff> findById(int staffId);
    Optional<Staff> findByUserId(int userId);
    List<Staff> findAll(Integer hotelId, String status);
    int updateHotel(int staffId, int hotelId);
    int updateStatus(int staffId, String status);
    boolean existsById(int staffId);
}
