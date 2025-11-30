package com.hotel.management.roomfacilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/room-facilities")
public class RoomFacilitiesController {
	@Autowired
    private  RoomFacilitiesService service;

    

    @GetMapping("/{id}")
    public RoomFacilities getFacility(@PathVariable int id) {
        return service.getFacility(id);
    }

    @GetMapping
    public List<RoomFacilities> getAllFacilities() {
        return service.getAllFacilities();
    }

    @PostMapping
    public void addFacility(@RequestBody RoomFacilities rf) {
        service.addFacility(rf);
    }

    @PutMapping("/{id}")
    public void updateFacility(@PathVariable int id, @RequestBody RoomFacilities rf) {
        rf.setRfId(id);
        service.updateFacility(rf);
    }

    @PatchMapping("/{id}/soft-delete")
    public void softDeleteFacility(@PathVariable int id) {
        service.softDeleteFacility(id);
    }
}
