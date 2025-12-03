package com.hotel.management.reports.roomoccupancy;

import java.util.List;

public interface RoomOccupancyService {

	List<RoomOccupancyDto> getOverallReport();

	List<RoomOccupancyDto> getReportForBranch(Integer branchId);
}