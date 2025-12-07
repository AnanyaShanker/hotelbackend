package com.hotel.management.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")


public class RoomController 

{

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

    @GetMapping("/available/dates")
    public ResponseEntity<List<Room>> getAvailableRoomsForDates(
            @RequestParam int branchId,
            @RequestParam int typeId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        // Convert LocalDate to Timestamp (start of day for checkIn, end of day for checkOut)
        LocalDateTime checkInDateTime = checkIn.atStartOfDay();
        LocalDateTime checkOutDateTime = checkOut.atTime(23, 59, 59);

        Timestamp checkInTs = Timestamp.valueOf(checkInDateTime);
        Timestamp checkOutTs = Timestamp.valueOf(checkOutDateTime);

        return ResponseEntity.ok(
                roomService.getAvailableRoomsForDates(branchId, typeId, checkInTs, checkOutTs)
        );
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
