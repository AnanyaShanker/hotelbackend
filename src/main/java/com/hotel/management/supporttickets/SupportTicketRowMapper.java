package com.hotel.management.supporttickets;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SupportTicketRowMapper implements RowMapper<SupportTicket> {

	@Override
	public SupportTicket mapRow(ResultSet rs, int rowNum) throws SQLException {
		SupportTicket t = new SupportTicket();

		t.setTicketId(rs.getInt("ticket_id"));
		t.setCustomerId(rs.getInt("customer_id"));

		Object asg = rs.getObject("assigned_staff_id");
		t.setAssignedStaffId(asg != null ? (Integer) asg : null);

		Object bid = rs.getObject("booking_id");
		t.setBookingId(bid != null ? (Integer) bid : null);

		Object fid = rs.getObject("facility_booking_id");
		t.setFacilityBookingId(fid != null ? (Integer) fid : null);

		t.setSubject(rs.getString("subject"));
		t.setCategory(rs.getString("category"));
		t.setStatus(rs.getString("status"));
		t.setDetails(rs.getString("details"));

		if (rs.getTimestamp("created_at") != null) {
			t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		}
		if (rs.getTimestamp("updated_at") != null) {
			t.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
		}

		return t;
	}
}