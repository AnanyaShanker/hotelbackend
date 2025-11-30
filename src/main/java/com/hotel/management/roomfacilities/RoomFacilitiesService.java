package com.hotel.management.roomfacilities;

import java.util.List;

public interface RoomFacilitiesService {
    RoomFacilities getFacility(int rfId);
    List<RoomFacilities> getAllFacilities();
    void addFacility(RoomFacilities rf);
    void updateFacility(RoomFacilities rf);
    void softDeleteFacility(int rfId);
}
