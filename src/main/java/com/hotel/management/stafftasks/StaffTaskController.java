package com.hotel.management.stafftasks;

import com.hotel.management.staff.StaffDTO;
import com.hotel.management.staff.StaffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stafftasks")
public class StaffTaskController {
	@Autowired
    private StaffTaskService taskService;
	@Autowired
    private StaffService staffService; 

   

    @PostMapping
    public ResponseEntity<StaffTaskDTO> createTask(@RequestBody StaffTaskDTO dto) {
        StaffTaskDTO created = taskService.createTask(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<StaffTaskDTO> getTask(@PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

  
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffTaskDTO>> getTasksByStaff(@PathVariable int staffId) {
        return ResponseEntity.ok(taskService.getTasksByStaff(staffId));
    }

   
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<StaffTaskDTO>> getTasksByRoom(@PathVariable int roomId) {
        return ResponseEntity.ok(taskService.getTasksByRoom(roomId));
    }

    
    @GetMapping
    public ResponseEntity<List<StaffTaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<StaffTaskDTO> updateStatus(
            @PathVariable int taskId,
            @RequestParam String status,
            @RequestParam(required = false) String remarks) {
        StaffTaskDTO updated = taskService.updateStatus(taskId, status, remarks);
        return ResponseEntity.ok(updated);
    }

    
    @GetMapping("/hotel/{hotelId}/staff")
    public ResponseEntity<List<StaffDTO>> getStaffByHotel(@PathVariable int hotelId) {
        List<StaffDTO> staffList = staffService.list(hotelId, null); // filter by hotelId
        return ResponseEntity.ok(staffList);
    }

  
    @PostMapping("/hotel/{hotelId}/assign")
    public ResponseEntity<StaffTaskDTO> assignTaskToStaff(
            @PathVariable int hotelId,
            @RequestBody StaffTaskDTO dto) {

       
        StaffDTO staff = staffService.getById(dto.getStaffId());
        if (staff.getHotelId() == null || !staff.getHotelId().equals(hotelId)) {
            throw new IllegalArgumentException("Staff does not belong to this hotel");
        }

        StaffTaskDTO created = taskService.createTask(dto);
        return ResponseEntity.status(201).body(created);
    }
}
