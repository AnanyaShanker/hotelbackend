package com.hotel.management.reports.roomoccupancy;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomOccupancyRowMapper implements RowMapper<RoomOccupancyDto> {

	@Override
	public RoomOccupancyDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		return new RoomOccupancyDto(rs.getInt("room_id"), rs.getString("room_number"), rs.getString("branch_name"),
				rs.getString("type_name"),
				rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
				rs.getInt("total_available_nights"), rs.getInt("booked_nights"), rs.getDouble("occupancy_percent"),
				rs.getString("current_status"), rs.getInt("total_bookings"), rs.getDouble("total_revenue"));
	}
}