package com.hotel.management.stafftasks;

import java.util.List;
import java.util.Optional;

public interface StaffTaskRepository {
    int save(StaffTask task);
    Optional<StaffTask> findById(int taskId);
    List<StaffTask> findByStaff(int staffId);
    List<StaffTask> findByRoom(int roomId);
    List<StaffTask> findAll();
    int updateStatus(int taskId, String status, String remarks);

    // New: fetch all tasks for a hotel (manager view). returns enriched rows (staff name, room number)
    List<StaffTask> findByHotel(int hotelId);
}
