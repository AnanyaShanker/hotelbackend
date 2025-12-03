package com.hotel.management.reports.roomoccupancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/reports/room-occupancy")
public class RoomOccupancyController {

	@Autowired
	private RoomOccupancyService service;

	@GetMapping("/overall")
	public List<RoomOccupancyDto> getOverallReport() {
		return service.getOverallReport();
	}

	@GetMapping("/branch/{branchId}")
	public List<RoomOccupancyDto> getBranchReport(@PathVariable Integer branchId) {
		return service.getReportForBranch(branchId);
	}
}