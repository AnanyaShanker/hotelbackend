package com.hotel.management.reports.housekeeping;

import com.hotel.management.reports.housekeeping.*;

import java.util.List;

public interface HousekeepingReportService {

    List<HousekeepingReportRowDto> getAdminReport();

    List<HousekeepingReportRowDto> getManagerReport(Integer managerUserId);

    HousekeepingSummaryDto getAdminSummary();

    HousekeepingSummaryDto getManagerSummary(Integer managerUserId);
}