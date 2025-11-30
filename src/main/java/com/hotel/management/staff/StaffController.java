package com.hotel.management.staff;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel.management.dto.ApiResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	@Autowired
    private StaffService staffService;

    

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDTO<StaffDTO>> createStaff(@RequestBody StaffDTO dto) {
        StaffDTO created = staffService.createStaff(dto.getUserId(), dto.getHotelId());
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(201, "Staff created successfully", created);
        return ResponseEntity.status(201).body(resp);
    }

   
    @PostMapping("/{userId}/assign")
    public ResponseEntity<ApiResponseDTO<StaffDTO>> assignHotel(
            @PathVariable int userId,
            @RequestBody StaffDTO dto) {
        StaffDTO created = staffService.createStaff(userId, dto.getHotelId());
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(201, "Staff assigned successfully", created);
        return ResponseEntity.status(201).body(resp);
    }

   
    @GetMapping("/{staffId}")
    public ResponseEntity<ApiResponseDTO<StaffDTO>> getById(@PathVariable int staffId) {
        StaffDTO staff = staffService.getById(staffId);
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(200, "OK", staff);
        return ResponseEntity.ok(resp);
    }

    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<StaffDTO>> getByUserId(@PathVariable int userId) {
        StaffDTO staff = staffService.getByUserId(userId);
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(200, "OK", staff);
        return ResponseEntity.ok(resp);
    }

    
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<StaffDTO>>> list(
            @RequestParam(required = false) Integer hotelId,
            @RequestParam(required = false) String status) {
        List<StaffDTO> list = staffService.list(hotelId, status);
        ApiResponseDTO<List<StaffDTO>> resp = new ApiResponseDTO<>(200, "OK", list);
        return ResponseEntity.ok(resp);
    }

   
    @PutMapping("/{staffId}/hotel")
    public ResponseEntity<ApiResponseDTO<StaffDTO>> updateHotel(
            @PathVariable int staffId,
            @RequestBody StaffDTO dto) {
        StaffDTO updated = staffService.updateHotel(staffId, dto.getHotelId());
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(200, "Hotel updated", updated);
        return ResponseEntity.ok(resp);
    }

   
    @PatchMapping("/{staffId}/status")
    public ResponseEntity<ApiResponseDTO<StaffDTO>> updateStatus(
            @PathVariable int staffId,
            @RequestBody StaffDTO dto) {
        StaffDTO updated = staffService.updateStatus(staffId, dto.getStatus());
        ApiResponseDTO<StaffDTO> resp = new ApiResponseDTO<>(200, "Status updated", updated);
        return ResponseEntity.ok(resp);
    }
}
