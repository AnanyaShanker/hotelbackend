package com.hotel.management.reports.roomrevenue;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class RoomRevenueServiceImpl implements RoomRevenueService {

	@Autowired
	private RoomRevenueRepository roomRevenueRepository;

	@Override
	public List<RoomRevenueDto> getOverallRevenueByRoomType() {
		return roomRevenueRepository.getRevenueByRoomType();
	}

	@Override
	public List<RoomRevenueDto> getRevenueByRoomTypeForManager(Integer managerUserId) {
		return roomRevenueRepository.getRevenueByRoomTypeForManager(managerUserId);
	}
}
