package com.hotel.management.reports.roomoccupancy;

import java.util.List;

public interface RoomOccupancyRepository {

    List<RoomOccupancyDto> getOverallRoomOccupancyReport();

    List<RoomOccupancyDto> getRoomOccupancyByBranch(Integer branchId);
}