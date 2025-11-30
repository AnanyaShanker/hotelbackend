package com.hotel.management.roomtypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
	@Autowired
    private  RoomTypeRepository repository;

    @Override
    public List<RoomTypes> getRoom() {
        return repository.findAll();
    }

    @Override
    public RoomTypes getRoomById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public RoomTypes addRoom(RoomTypes dto) {
    	Integer id = repository.insert(dto);
        dto.setTypeId(id);
        return dto;
    }

    @Override
    public RoomTypes updateRoom(Integer id, RoomTypes dto) {
        int updated = repository.update(id, dto);
        if (updated == 0) throw new IllegalArgumentException("Room type not found");
        dto.setTypeId(id);
        return dto;
    }

}
