package com.hotel.management.branches;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BranchRowMapper implements RowMapper<Branch> {

	@Override
	public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {

		Branch branch = new Branch();

		branch.setBranchId(rs.getInt("branch_id"));
		branch.setName(rs.getString("name"));
		branch.setLocation(rs.getString("location"));
		branch.setContactNumber(rs.getString("contact_number"));

		int managerId = rs.getInt("manager_id");
		branch.setManagerId(rs.wasNull() ? null : managerId);

		branch.setTotalRooms(rs.getInt("total_rooms"));
		branch.setStatus(rs.getString("status"));

		Timestamp ts = rs.getTimestamp("created_at");
		branch.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);

		return branch;
	}
}
