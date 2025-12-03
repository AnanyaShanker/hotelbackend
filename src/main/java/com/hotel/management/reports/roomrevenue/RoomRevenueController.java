package com.hotel.management.reports.roomrevenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/reports/room-revenue")
public class RoomRevenueController {

	@Autowired
	private RoomRevenueService roomRevenueService;

	/**
	 * Admin: overall revenue by room type (Single, Double, Suite)
	 */
	@GetMapping
	public ResponseEntity<List<RoomRevenueDto>> getOverallRevenue() {
		List<RoomRevenueDto> list = roomRevenueService.getOverallRevenueByRoomType();
		return ResponseEntity.ok(list);
	}

	/**
	 * Manager: revenue for room types but only for branches manager manages.
	 */
	@GetMapping("/manager/{managerUserId}")
	public ResponseEntity<List<RoomRevenueDto>> getRevenueForManager(@PathVariable Integer managerUserId) {
		List<RoomRevenueDto> list = roomRevenueService.getRevenueByRoomTypeForManager(managerUserId);
		return ResponseEntity.ok(list);
	}
}
