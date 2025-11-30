package com.hotel.management.roomfacilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomFacilitiesServiceImpl implements RoomFacilitiesService {

	@Autowired
    private RoomFacilitiesRepository repository;


    @Override
    public RoomFacilities getFacility(int rfId) {
        return repository.findById(rfId);
    }

    @Override
    public List<RoomFacilities> getAllFacilities() {
        return repository.findAll();
    }

    @Override
    public void addFacility(RoomFacilities rf) {
        repository.save(rf);
    }

    @Override
    public void updateFacility(RoomFacilities rf) {
        repository.update(rf);
    }

    @Override
    public void softDeleteFacility(int rfId) {
        repository.softDelete(rfId);
    }
}
