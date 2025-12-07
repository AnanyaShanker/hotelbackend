package com.hotel.management.stafftasks;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class StaffTaskServiceImpl implements StaffTaskService {
 
    @Autowired
    private StaffTaskRepository repo;
 
    @Autowired
    private ApplicationEventPublisher eventPublisher;
 
    @Override
    public StaffTaskDTO createTask(StaffTaskDTO dto) {
        StaffTask task = StaffTaskMapper.toEntity(dto);
 
        // Default status and assignedAt behaviors
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("PENDING");
        }
 
        int id = repo.save(task);
        task.setTaskId(id);
 
        // Try to enrich returned DTO by refetching
        StaffTask createdEntity = repo.findById(id).orElse(task);
        StaffTaskDTO createdDTO = StaffTaskMapper.toDTO(createdEntity);
 
        // Publish event (optional)
        try {
            Integer staffUserId = createdEntity.getStaffUserId();
            String roomNumber = createdEntity.getRoomNumber();
            if (staffUserId != null) {
                StaffTaskCreatedEvent event = new StaffTaskCreatedEvent(this, createdDTO, staffUserId, roomNumber);
                eventPublisher.publishEvent(event);
            }
        } catch (Exception ex) {
            // swallow errors from notification publishing
            System.err.println("Failed to publish StaffTaskCreatedEvent: " + ex.getMessage());
        }
 
        return createdDTO;
    }
 
    @Override
    public StaffTaskDTO getTask(int taskId) {
        StaffTask t = repo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return StaffTaskMapper.toDTO(t);
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
 
    @Override
    public List<StaffTaskDTO> getTasksByHotel(int hotelId) {
        return repo.findByHotel(hotelId).stream().map(StaffTaskMapper::toDTO).collect(Collectors.toList());
    }
}
 
 