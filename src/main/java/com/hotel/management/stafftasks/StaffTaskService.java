package com.hotel.management.stafftasks;

import java.util.List;

public interface StaffTaskService {
    StaffTaskDTO createTask(StaffTaskDTO dto);
    StaffTaskDTO getTask(int taskId);
    List<StaffTaskDTO> getTasksByStaff(int staffId);
    List<StaffTaskDTO> getTasksByRoom(int roomId);
    List<StaffTaskDTO> getAllTasks();
    StaffTaskDTO updateStatus(int taskId, String status, String remarks);

    // new
    List<StaffTaskDTO> getTasksByHotel(int hotelId);
}