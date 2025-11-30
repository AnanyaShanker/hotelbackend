package com.hotel.management.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
	@Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(room));
    }
    
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

   
    @GetMapping("/available/all")
    public ResponseEntity<List<Room>> getAllAvailableRooms() {
        return ResponseEntity.ok(roomService.getAllAvailableRooms());
    }

   
    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable int roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

   
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Room>> getRoomsByBranch(@PathVariable int branchId) {
        return ResponseEntity.ok(roomService.getRoomsByBranch(branchId));
    }

    
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable int typeId) {
        return ResponseEntity.ok(roomService.getRoomsByType(typeId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam int branchId,
            @RequestParam int typeId) {
        return ResponseEntity.ok(roomService.getAvailableRooms(branchId, typeId));
    }

   
    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable int roomId, @RequestBody Room room) {
        room.setRoomId(roomId);
        return ResponseEntity.ok(roomService.updateRoom(room));
    }

   
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> softDeleteRoom(@PathVariable int roomId) {
        roomService.softDeleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    
    @PatchMapping("/{roomId}/status")
    public ResponseEntity<Void> updateRoomStatus(@PathVariable int roomId, @RequestBody StatusUpdateRequest request) {
    	 roomService.changeRoomStatus(roomId, request.getStatus());
        return ResponseEntity.ok().build();
    }
 

}
