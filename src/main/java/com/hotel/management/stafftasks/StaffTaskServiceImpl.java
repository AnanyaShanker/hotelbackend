package com.hotel.management.stafftasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffTaskServiceImpl implements StaffTaskService {

	@Autowired
    private StaffTaskRepository repo;


    @Override
    public StaffTaskDTO createTask(StaffTaskDTO dto) {
        StaffTask task = StaffTaskMapper.toEntity(dto);
        task.setStatus("PENDING");
        int id = repo.save(task);
        task.setTaskId(id);
        return StaffTaskMapper.toDTO(task);
    }

    @Override
    public StaffTaskDTO getTask(int taskId) {
        StaffTask task = repo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return StaffTaskMapper.toDTO(task);
    }

    @Override
    public List<StaffTaskDTO> getTasksByStaff(int staffId) {
        return repo.findByStaff(staffId).stream().map(StaffTaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<StaffTaskDTO> getTasksByRoom(int roomId) {
        return repo.findByRoom(roomId).stream().map(StaffTaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<StaffTaskDTO> getAllTasks() {
        return repo.findAll().stream().map(StaffTaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public StaffTaskDTO updateStatus(int taskId, String status, String remarks) {
        repo.updateStatus(taskId, status, remarks);
        return getTask(taskId);
    }
}
