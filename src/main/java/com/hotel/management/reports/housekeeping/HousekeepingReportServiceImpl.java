package com.hotel.management.reports.housekeeping;

import com.hotel.management.reports.housekeeping.*;
import com.hotel.management.reports.housekeeping.HousekeepingReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousekeepingReportServiceImpl implements HousekeepingReportService {

    private final HousekeepingReportRepository repo;

    public HousekeepingReportServiceImpl(HousekeepingReportRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<HousekeepingReportRowDto> getAdminReport() {
        return repo.getAdminReport();
    }

    @Override
    public List<HousekeepingReportRowDto> getManagerReport(Integer managerUserId) {
        return repo.getManagerReport(managerUserId);
    }

    @Override
    public HousekeepingSummaryDto getAdminSummary() {
        return repo.getAdminSummary();
    }

    @Override
    public HousekeepingSummaryDto getManagerSummary(Integer managerUserId) {
        return repo.getManagerSummary(managerUserId);
    }
}

