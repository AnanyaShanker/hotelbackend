package com.hotel.management.reports.housekeeping;

import com.hotel.management.reports.housekeeping.*;
import com.hotel.management.reports.housekeeping.HousekeepingReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/housekeeping")
public class HousekeepingReportController {

    private final HousekeepingReportService service;

    public HousekeepingReportController(HousekeepingReportService service) {
        this.service = service;
    }

    @GetMapping("/admin")
    public List<HousekeepingReportRowDto> getAdminReport() {
        return service.getAdminReport();
    }

    @GetMapping("/admin/summary")
    public HousekeepingSummaryDto getAdminSummary() {
        return service.getAdminSummary();
    }

    @GetMapping("/manager/{managerId}")
    public List<HousekeepingReportRowDto> getManagerReport(@PathVariable Integer managerId) {
        return service.getManagerReport(managerId);
    }

    @GetMapping("/manager/{managerId}/summary")
    public HousekeepingSummaryDto getManagerSummary(@PathVariable Integer managerId) {
        return service.getManagerSummary(managerId);
    }
}