package com.hotel.management.reports.roomoccupancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOccupancyServiceImpl implements RoomOccupancyService {

	@Autowired
	private RoomOccupancyRepository repository;

	@Override
	public List<RoomOccupancyDto> getOverallReport() {
		return repository.getOverallRoomOccupancyReport();
	}

	@Override
	public List<RoomOccupancyDto> getReportForBranch(Integer branchId) {
		return repository.getRoomOccupancyByBranch(branchId);
	}
}
