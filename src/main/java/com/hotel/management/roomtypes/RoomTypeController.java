package com.hotel.management.roomtypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping({"/api/roomtypes", "/api/room-types"})
public class RoomTypeController {

	@Autowired
    private RoomTypeService service;


    @GetMapping
    public ResponseEntity<List<RoomTypes>> getRoom() {
        return ResponseEntity.ok(service.getRoom());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypes> getRoomById(@PathVariable Integer id) {
        RoomTypes dto = service.getRoomById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RoomTypes> addRoom(@RequestBody RoomTypes dto) {
        RoomTypes created = service.addRoom(dto);
        return ResponseEntity.created(URI.create("/api/roomtypes/" + created.getTypeId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypes> updateRoom(@PathVariable Integer id, @RequestBody RoomTypes dto) {
        RoomTypes updated = service.updateRoom(id, dto);
        return ResponseEntity.ok(updated);
    }


}
