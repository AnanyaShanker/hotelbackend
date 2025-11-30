package com.hotel.management.roomfacilities;

import java.util.List;

public interface RoomFacilitiesRepository {
    RoomFacilities findById(int rfId);
    List<RoomFacilities> findAll();
    void save(RoomFacilities rf);
    void update(RoomFacilities rf);
    void softDelete(int rfId); 
}
