package com.hotel.management.reports.housekeeping;

import com.hotel.management.reports.housekeeping.HousekeepingReportRowDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HousekeepingReportRowMapper implements RowMapper<HousekeepingReportRowDto> {

	@Override
	public HousekeepingReportRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		return new HousekeepingReportRowDto(rs.getInt("task_id"), rs.getString("staff_name"),
				rs.getString("room_number"), rs.getString("branch_name"), rs.getString("task_type"),
				rs.getString("status"), rs.getTimestamp("assigned_at").toLocalDateTime(),
				rs.getTimestamp("completed_at") != null ? rs.getTimestamp("completed_at").toLocalDateTime() : null,
				rs.getLong("time_taken_minutes"));
	}
}
